import { StatusBar } from "expo-status-bar";
import * as React from "react";
import { StyleSheet, Text, View } from "react-native";
import useCachedResources from "./src/hook/useCachedResources";
import {
  Provider as PaperProvider,
  DefaultTheme as PaperLightTheme,
  DarkTheme as PaperDarkTheme,
  DefaultTheme,
} from "react-native-paper";
import { SafeAreaProvider } from "react-native-safe-area-context";
import InsideApp from "./src/InsideApp";

export default function App(): React.ReactElement {
  const isLoadingComplete = useCachedResources();
  const [theme, setTheme] = React.useState(DefaultTheme);
  React.useEffect(() => {
    console.log("isLoadingComplete:", isLoadingComplete);
  }, [isLoadingComplete]);

  const paperTheme = React.useMemo(() => {
    const t = theme.dark ? PaperDarkTheme : PaperLightTheme;

    return {
      ...t,
      colors: {
        ...t.colors,
        ...theme.colors,
        surface: theme.colors.card,
        accent: theme.dark ? "rgb(255, 55, 95)" : "rgb(255, 45, 85)",
      },
    };
  }, [theme.colors, theme.dark]);

  if (!isLoadingComplete) {
    return null;
  } else {
    return (
      <SafeAreaProvider>
        <PaperProvider theme={paperTheme}>
          <InsideApp />
        </PaperProvider>
      </SafeAreaProvider>
    );
  }
}
