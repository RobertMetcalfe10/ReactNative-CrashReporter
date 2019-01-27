import React, {Component} from 'react';
import {StyleSheet, Button, View} from 'react-native';
import stacktraceParser from 'stacktrace-parser';

export default class App extends Component<Props> {

  static originalHandler = ErrorUtils.getGlobalHandler();

  constructor() {
      super();
      ErrorUtils.setGlobalHandler(App.firebaseGlobalHandler)
  }

  // static parseErrorStack = (error) => {
  //   if (!error || !error.stack) {
  //     return [];
  //     }
  //     return Array.isArray(error.stack) ? error.stack : stacktraceParser.parse(error.stack);
  // };

  static async firebaseGlobalHandler(error, isFatal) {
    // const stack = App.parseErrorStack(error);
    console.log(error);
    if (isFatal) {
        App.originalHandler(error, true);
    } else {
        App.originalHandler(error, false);
    }
  }

    static throwException() {
        throw undefined
    }

  render() {
    return (
      <View style={styles.container}>
          <Button title={"Throw Exception"} onPress={App.throwException}/>
      </View>
    );
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
