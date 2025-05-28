import 'package:flutter/material.dart';
import 'package:mobile_app/widgets/app_text.dart';

class AppTextButton extends StatelessWidget {
  const AppTextButton({super.key, required this.label, this.onPressed});

  final String label;
  final void Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onPressed,
      child: AppText(text: label, fontSize: 20, color: Colors.blue),
    );
  }
}
