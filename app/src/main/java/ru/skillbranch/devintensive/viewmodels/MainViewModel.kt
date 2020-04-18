package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.DataGenerator

class MainViewModel : ViewModel() {

    private val chatRepository = ChatRepository

    //Transformations следит за изменением LiveData и получает их (подписка на источник данных)
    var chats = Transformations.map(chatRepository.loadChats()){ chats ->
        return@map chats.filter { !it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id.toInt() }
    }

//    var chats = mutableLiveData(loadChats())
//
    fun getChatData(): LiveData<List<ChatItem>> {
        return chats
    }
//
//    private fun loadChats(): List<ChatItem> {
//        val chats = chatRepository.loadChats()
//        return chats.map { it.toChatItem() }
//            .sortedBy { it.id.toInt() }
//    }

//    fun addItems(){
//        val newChatItems = DataGenerator.generateChatsWithOffset(chats.value!!.size, 5).map { it.toChatItem() }
//        val copy = chats.value!!.toMutableList()
//        copy.addAll(newChatItems)
//        chats.value = copy.sortedBy { it.id.toInt() }
//    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }
}