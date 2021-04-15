import { StackNavigationHelpers } from "@react-navigation/stack/lib/typescript/src/types";
import * as React from "react";
import * as Paper from "react-native-paper";
import * as RN from "react-native";
import { useDispatch } from "react-redux";
import CheckLoginStrategy, {
  CheckLoginBuilder,
} from "../../util/CheckLoginStrategy";
import LoginHelperStrategy, {
  LoginHelperBuilder,
} from "../../util/LoginHelperStrategy";
import Responsive from "../util/Responsive";
import styles from "../../style/common";

export default function LoginScreen(props: {
  navigation: StackNavigationHelpers;
}): React.ReactElement {
  const { navigation } = props;
  const [username, setUsername] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [error, setError] = React.useState("");
  const [CheckLoginStrategy, _] = React.useState<CheckLoginStrategy>(
    CheckLoginBuilder.build()
  );
  const [LoginHelperStrategy, __] = React.useState<LoginHelperStrategy>(
    LoginHelperBuilder.build()
  );
  const [isLogin, setIsLogin] = React.useState<Boolean>(
    CheckLoginStrategy.checkLogin()
  );

  React.useEffect(() => {
    if (isLogin) {
      onLoginSucceed();
    }
  }, [isLogin]);

  const onLoginSucceed = () => {
    if (navigation.canGoBack()) {
      navigation.goBack();
    } else {
      navigation.push("home");
    }
  };
  const noArgumentError: string = "이메일 혹은 비밀번호가 입력되지 않았습니다.";
  const onLoginClick = (): void => {
    console.log({ username, password });
    if (isLogin) {
      onLoginSucceed();
      return;
    }
    if (!username || !password) {
      setError(noArgumentError);
      return;
    }
    if (error === noArgumentError) {
      setError("");
    }
    if (
      LoginHelperStrategy.login({
        username,
        password,
      })
    ) {
      setIsLogin(true);
    }
    setUsername("");
    setPassword("");
  };
  const errorComponent = error ? <Paper.Text>{error}</Paper.Text> : null;
  return (
    <Responsive style={styles.container}>
      <Paper.Text style={styles.title}>Please Login.</Paper.Text>
      <RN.View>
        <Paper.TextInput
          label="Username"
          placeholder="Username"
          textContentType="username"
          autoCompleteType="username"
          autoCapitalize="none"
          value={username}
          onChangeText={(text: string) => setUsername(text)}
          returnKeyType="next"
        />
        <Paper.TextInput
          secureTextEntry
          label="Password"
          placeholder="Password"
          textContentType="password"
          autoCompleteType="password"
          autoCapitalize="none"
          value={password}
          onChangeText={(text: string) => setPassword(text)}
          onSubmitEditing={onLoginClick}
          returnKeyType="done"
        />
        {errorComponent}
        <Paper.Button
          mode="contained"
          onPress={(): void => {
            onLoginClick();
          }}
        >
          Login
        </Paper.Button>
        <RN.View>
          <Paper.Text>Register? </Paper.Text>
          <Paper.Button
            mode="contained"
            onPress={(): void => {
              console.log(navigation);
              navigation.push("register");
            }}
          >
            Register
          </Paper.Button>
        </RN.View>
      </RN.View>
    </Responsive>
  );
}
