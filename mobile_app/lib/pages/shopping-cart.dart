import 'package:flutter/material.dart';
import 'package:mobile_app/widgets/app_text.dart';

class ShoppingCartPage extends StatefulWidget {
  const ShoppingCartPage({super.key});

  @override
  State<ShoppingCartPage> createState() => _ShoppingCartPageState();
}

class _ShoppingCartPageState extends State<ShoppingCartPage> {
  @override
  Widget build(BuildContext context) {
    return Center(child: AppText(text: "Panier"));
  }
}
