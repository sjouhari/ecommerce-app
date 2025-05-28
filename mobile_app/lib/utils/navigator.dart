import 'package:flutter/material.dart';

class CustomNavigator {
  static void navigateTo(
    BuildContext context,
    Widget page, {
    bool replace = false,
  }) {
    final route = PageRouteBuilder(
      pageBuilder: (_, __, ___) => page,
      transitionDuration: Duration(milliseconds: 0),
      transitionsBuilder: (_, Animation<double> animation, __, Widget child) {
        return child;
      },
    );
    replace
        ? Navigator.of(context).pushReplacement(route)
        : Navigator.of(context).push(route);
  }

  static void goBack(BuildContext context) {
    return Navigator.of(context).pop();
  }
}
