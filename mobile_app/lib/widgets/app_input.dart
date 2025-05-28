import 'package:flutter/material.dart';

class AppInput extends StatelessWidget {
  const AppInput({
    super.key,
    required this.label,
    required this.controller,
    this.validator,
    this.obscureText = false,
    this.keyboardType,
    this.prefixIcon,
    this.autofillHints,
  });

  final String label;
  final TextEditingController controller;
  final String? Function(String?)? validator;
  final bool obscureText;
  final TextInputType? keyboardType;
  final IconData? prefixIcon;
  final Iterable<String>? autofillHints;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: controller,
      obscureText: obscureText,
      obscuringCharacter: '*',
      validator: validator,
      keyboardType: keyboardType,
      style: const TextStyle(color: Colors.black),
      autofillHints: [AutofillHints.email],
      decoration: InputDecoration(
        hintText: label,
        hintStyle: TextStyle(color: Colors.black),
        contentPadding: const EdgeInsets.fromLTRB(0, 12, 12, 12),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.all(Radius.circular(10)),
        ),
        prefixIcon:
            prefixIcon != null
                ? Icon(prefixIcon, color: Colors.blueGrey)
                : null,
        enabledBorder: OutlineInputBorder(
          borderSide: const BorderSide(
            width: 0.7,
            color: Color.fromARGB(92, 158, 158, 158),
          ),
          borderRadius: BorderRadius.circular(10),
        ),
        focusedBorder: OutlineInputBorder(
          borderSide: const BorderSide(color: Colors.blue, width: 1.0),
          borderRadius: BorderRadius.circular(10),
        ),
        errorBorder: OutlineInputBorder(
          borderSide: const BorderSide(color: Colors.red, width: 1.0),
          borderRadius: BorderRadius.circular(10),
        ),
        focusedErrorBorder: OutlineInputBorder(
          borderSide: const BorderSide(color: Colors.red, width: 1.0),
          borderRadius: BorderRadius.circular(10),
        ),
        errorStyle: const TextStyle(fontSize: 15, height: 0),
      ),
    );
  }
}
