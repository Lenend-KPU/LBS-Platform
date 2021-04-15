import * as React from "react";
import * as Paper from "react-native-paper";
import * as RN from "react-native";
import { StackNavigationHelpers } from "@react-navigation/stack/lib/typescript/src/types";

export default function BackNavigatorComponent(props: {
  navigation: StackNavigationHelpers;
}) {
  const { navigation } = props;
  const color = navigation.canGoBack()
    ? Paper.Colors.blue400
    : Paper.Colors.grey400;
  return (
    <RN.View
      style={{
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Paper.Button
        onPress={() => {
          if (navigation.canGoBack()) {
            navigation.goBack();
          }
        }}
        color={color}
      >
        Back
      </Paper.Button>
    </RN.View>
  );
}
