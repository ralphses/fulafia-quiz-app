// HomeScreen.js
import React, { useState } from 'react';
import { View, Text, Button } from 'react-native';

export default function HomeScreen({ navigation }) {
  const [disableMinimize, setDisableMinimize] = useState(false);

  const toggleMinimize = () => {
    setDisableMinimize(!disableMinimize);
  };

  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text>Home Screen</Text>
      <Button
        title={`Minimize is ${disableMinimize ? 'disabled' : 'enabled'}`}
        onPress={toggleMinimize}
      />
      <Button
        title="Go to Other Screen"
        onPress={() => navigation.navigate('Other')}
      />
    </View>
  );
}
