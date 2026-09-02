package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

data class Contact(
    val name: String,
    val lastMessage: String,
    val badgeCount: Int = 0,
    val avatarRes: Int = 0
)

class ChatsActivity : AppCompatActivity() {

    private lateinit var lvContacts: ListView
    private lateinit var etSearch: EditText
    private lateinit var btnBack: ImageButton
    private lateinit var adapter: ContactAdapter

    private val contacts = listOf(
        Contact("John Joshua", "Thanks for your service", 1),
        Contact("Chinonso James", "Alright, I will be waiting", 0),
        Contact("Raph Ron", "Thanks for your service", 5),
        Contact("Joy Ezekiel", "Thanks for your service", 0),
        Contact("Joy Ezekiel", "Thanks for your service", 1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        lvContacts = findViewById(R.id.lvContacts)
        etSearch = findViewById(R.id.etSearch)
        btnBack = findViewById(R.id.btnBack)

        adapter = ContactAdapter(this, contacts.toMutableList())
        lvContacts.adapter = adapter

        // Back button goes to Login screen
        btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Search filter
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase()
                val filtered = if (query.isEmpty()) {
                    contacts.toMutableList()
                } else {
                    contacts.filter {
                        it.name.lowercase().contains(query) ||
                        it.lastMessage.lowercase().contains(query)
                    }.toMutableList()
                }
                adapter.updateData(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Click on contact -> go to detail screen
        lvContacts.setOnItemClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position) as Contact
            val intent = Intent(this, ContactDetailActivity::class.java).apply {
                putExtra("name", contact.name)
                putExtra("lastMessage", contact.lastMessage)
                putExtra("badge", contact.badgeCount)
            }
            startActivity(intent)
        }
    }
}
