import React, {Component} from 'react';
import {StyleSheet, Button, View} from 'react-native';
import stacktraceParser from 'stacktrace-parser';
import {NativeModules} from 'react-native';

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
      try {
          if (isFatal) {
              NativeModules.FirebaseReporter.reportErrorFirebase(error.toString(), (err) => {console.log(err)}, (msg) => {console.log(msg)});
              App.originalHandler(error, true);
          } else {
              App.originalHandler(error, false);
          }
      } catch (e) {
          console.log(e)
      }
  }

    static throwException() {
        throw Error("FirebaseError")
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
