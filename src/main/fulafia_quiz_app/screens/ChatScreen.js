// ChatScreen.js
import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import { Stomp } from 'stompjs'; // WebSocket library

const ChatScreen = () => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [stompClient, setStompClient] = useState(null);

  const connectWebSocket = () => {
    const socket = new WebSocket('wss://your-backend-url/ws');
    const client = Stomp.over(socket);

    client.connect({}, () => {
      client.subscribe('/topic/public', (message) => {
        const chatMessage = JSON.parse(message.body);
        setMessages([...messages, chatMessage]);
      });
      setStompClient(client);
    });
  };

  const handleSendMessage = () => {
    const message = {
      sender: 'customer',
      content: newMessage,
    };
    stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(message));
    setNewMessage('');
  };

  return (
    <View style={styles.container}>
      <View style={styles.messageContainer}>
        {messages.map((message, index) => (
          <View key={index} style={styles.message}>
            <Text style={styles.sender}>{message.sender}:</Text>
            <Text style={styles.content}>{message.content}</Text>
          </View>
        ))}
      </View>
      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          placeholder="Type your message..."
          value={newMessage}
          onChangeText={(text) => setNewMessage(text)}
        />
        <TouchableOpacity style={styles.sendButton} onPress={handleSendMessage}>
          <Text style={styles.sendButtonText}>Send</Text>
        </TouchableOpacity>
      </View>
      {stompClient ? null : (
        <TouchableOpacity style={styles.connectButton} onPress={connectWebSocket}>
          <Text style={styles.connectButtonText}>Connect to Chat</Text>
        </TouchableOpacity>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  messageContainer: {
    flex: 1,
    marginBottom: 10,
  },
  message: {
    flexDirection: 'row',
    marginBottom: 5,
  },
  sender: {
    fontWeight: 'bold',
  },
  content: {
    marginLeft: 10,
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  input: {
    flex: 1,
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 20,
    paddingHorizontal: 16,
  },
  sendButton: {
    marginLeft: 10,
    backgroundColor: 'blue',
    borderRadius: 20,
    paddingVertical: 10,
    paddingHorizontal: 20,
  },
  sendButtonText: {
    color: 'white',
  },
  connectButton: {
    backgroundColor: 'green',
    borderRadius: 20,
    paddingVertical: 10,
    paddingHorizontal: 20,
    alignSelf: 'center',
  },
  connectButtonText: {
    color: 'white',
  },
});

export default ChatScreen;
