import * as React from "react";
import * as Paper from "react-native-paper";
import * as RN from "react-native";
import { useDispatch, useSelector } from "react-redux";
import { StackNavigationHelpers } from "@react-navigation/stack/lib/typescript/src/types";
import { useState } from "react";
import { useRoute } from "@react-navigation/native";
import CheckLoginStrategy, {
  CheckLoginBuilder,
} from "../../util/CheckLoginStrategy";
import LoginHelperStrategy from "../../util/LoginHelperStrategy";
import DummyCheckLogin from "../../util/DummyCheckLogin";

export default function CheckLoginComponent(props: {
  navigation: StackNavigationHelpers;
}) {
  const { navigation } = props;
  const [CheckLoginStrategy, _] = React.useState<DummyCheckLogin>(
    new DummyCheckLogin()
  );
  const [isLogin, setIsLogin] = React.useState<Boolean>(
    CheckLoginStrategy.checkLogin()
  );
  const route = useRoute();
  React.useEffect(() => {
    if (!isLogin && !(route.name === "login" || route.name === "register")) {
      navigation.push("login");
    }
  }, [isLogin]);
  const logout = () => {
    if (CheckLoginStrategy.logout()) {
      setIsLogin(false);
    }
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
