import * as React from "react";
import {
  InitialState,
  NavigationContainer,
  NavigationContainerRef,
} from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import AsyncStorage from "@react-native-community/async-storage";
import LinkingPrefixes from "./LinkingPrefixes";
import { StatusBar } from "expo-status-bar";
import BackNavigatorComponent from "./component/util/BackNavigatorComponent";
import CheckLoginComponent from "./component/util/CheckLoginComponent";

const Stack = createStackNavigator();
const NAVIGATION_PERSISTENCE_KEY = "NAVIGATION_STATE";
export default function InsideApp(props: Object) {
  const [initialState, setInitialState] = React.useState<
    InitialState | undefined
  >();
  const navigationRef = React.useRef<NavigationContainerRef>(null);
  return (
    <NavigationContainer
      ref={navigationRef}
      initialState={initialState}
      onStateChange={(state) =>
        AsyncStorage?.setItem(NAVIGATION_PERSISTENCE_KEY, JSON.stringify(state))
      }
      linking={LinkingPrefixes}
    >
      <StatusBar />
      <Stack.Navigator initialRouteName="main">
        <Stack.Screen
          name="login"
          component={LoginScreen}
          options={({ navigation, route }) => ({
            headerLeft: (props) => (
              <BackNavigatorComponent navigation={navigation} />
            ),
            headerRight: (props) => (
              <CheckLoginComponent navigation={navigation} />
            ),
          })}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
