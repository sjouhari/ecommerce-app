import 'package:flutter/material.dart';
import 'package:mobile_app/widgets/app_text.dart';

class AppButton extends StatelessWidget {
  const AppButton({super.key, required this.label, this.onPressed});

  final String label;
  final void Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        minimumSize: const Size(double.maxFinite, 50),
        backgroundColor: Colors.blue,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
      ),
      onPressed: onPressed,
      child: AppText(text: label, color: Colors.white),
    );
  }
}
