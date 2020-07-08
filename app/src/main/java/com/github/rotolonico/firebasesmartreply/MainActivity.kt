package com.github.rotolonico.firebasesmartreply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var conversation = ArrayList<FirebaseTextMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton.setOnClickListener {
            addMessage(messageText.text.toString())
        }

        hintsButton.setOnClickListener {
            getHints()
        }

        clearButton.setOnClickListener {
            conversation = ArrayList()

            hint0Button.visibility = View.GONE
            hint1Button.visibility = View.GONE
            hint2Button.visibility = View.GONE
            errorText.text = ""
        }

        hint0Button.setOnClickListener {
            addMessage(hint0Button.text.toString())
        }

        hint1Button.setOnClickListener {
            addMessage(hint1Button.text.toString())
        }

        hint2Button.setOnClickListener {
            addMessage(hint2Button.text.toString())
        }
    }

    private fun addMessage(text : String){
        conversation.add(
            FirebaseTextMessage.createForRemoteUser(
            text, System.currentTimeMillis(), nameText.text.toString()))
    }

    private fun getHints(){
        val smartReply = FirebaseNaturalLanguage.getInstance().smartReply
        smartReply.suggestReplies(conversation)
            .addOnSuccessListener { result ->
                if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                    errorText.text = "Language not supported"
                } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                    hint0Button.text = result.suggestions[0].text
                    hint1Button.text = result.suggestions[1].text
                    hint2Button.text = result.suggestions[2].text

                    hint0Button.visibility = View.VISIBLE
                    hint1Button.visibility = View.VISIBLE
                    hint2Button.visibility = View.VISIBLE
                    errorText.text = ""
                }
            }
            .addOnFailureListener {
                errorText.text = it.toString()
            }
    }
}
