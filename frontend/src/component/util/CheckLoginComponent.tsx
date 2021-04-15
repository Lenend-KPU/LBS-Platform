import * as React from "react";
import * as Paper from "react-native-paper";
import * as RN from "react-native";
import { useDispatch, useSelector } from "react-redux";
import { StackNavigationHelpers } from "@react-navigation/stack/lib/typescript/src/types";
import { useState } from "react";
import { useRoute } from "@react-navigation/native";
import CheckLoginStrategy, {
  checkLoginBuilder,
} from "../../util/CheckLoginStrategy";

export default function CheckLoginComponent(props: {
  navigation: StackNavigationHelpers;
}) {
  const { navigation } = props;
  const [CheckLoginStrategy, _] = React.useState<CheckLoginStrategy>(
    checkLoginBuilder.build()
  );
  const isLogin = CheckLoginStrategy.checkLogin();
  const route = useRoute();
  const logout = () => {
    //   TODO
    return false;
  };

  const onLogoutClick = () => {
    logout();
  };

  const login = () => {
    navigation.push("login");
  };

  const onLoginClick = () => {
    login();
  };

  const LogoutButton = !isLogin ? (
    route.name !== "login" ? (
      <Paper.Button mode="contained" onPress={onLoginClick}>
        Login
      </Paper.Button>
    ) : null
  ) : (
    <Paper.Button mode="contained" onPress={onLogoutClick}>
      Logout
    </Paper.Button>
  );

  return (
    <RN.View
      style={{
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      {LogoutButton}
    </RN.View>
  );
}
