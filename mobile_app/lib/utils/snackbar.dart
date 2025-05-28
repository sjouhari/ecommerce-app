import 'package:flutter/material.dart';
import 'package:mobile_app/widgets/app_text.dart';

void showCustomSnackBar(
  BuildContext context,
  String message, {
  bool isError = false,
}) {
  final snackBar = SnackBar(
    content: Row(
      children: [
        Icon(isError ? Icons.error : Icons.check_circle, color: Colors.white),
        const SizedBox(width: 10),
        Expanded(child: AppText(text: message, color: Colors.white)),
      ],
    ),
    backgroundColor: isError ? Colors.red : Colors.green,
    behavior: SnackBarBehavior.floating,
    margin: const EdgeInsets.all(16),
    duration: const Duration(seconds: 5),
    showCloseIcon: true,
    closeIconColor: Colors.white,
    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
  );

  ScaffoldMessenger.of(context).showSnackBar(snackBar);
}
