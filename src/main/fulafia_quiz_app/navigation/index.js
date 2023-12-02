import {createStackNavigator} from '@react-navigation/stack';
import 'react-native-gesture-handler';
import Home from '../screens/Home';
import Quiz from '../screens/Quiz';
import Result from '../screens/Result';
import StartScreen from '../screens/Start';
import HomeScreen from '../screens/Test';
import ChatScreen from '../screens/ChatScreen';

const Stack = createStackNavigator();

function NavigationStack() {
  return (
    <Stack.Navigator>
      <Stack.Screen name='Chat' component={ChatScreen} />
      <Stack.Screen
        name="Home"
        component={Home}
        options={{headerShown: false}}
      />
      <Stack.Screen name="Quiz" component={Quiz} />
      <Stack.Screen name="Start" component={StartScreen} />
      <Stack.Screen name="Result" component={Result} />
    </Stack.Navigator>
  );
}

export default NavigationStack;
