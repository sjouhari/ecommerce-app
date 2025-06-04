/// ***********  ✨ Windsurf Command 🌟  ************
library;

import 'package:flutter/material.dart';
import 'package:mobile_app/pages/auth/login.dart';
import 'package:mobile_app/providers/auth_provider.dart';
import 'package:mobile_app/utils/navigator.dart';
import 'package:mobile_app/widgets/app_button.dart';
import 'package:mobile_app/widgets/app_text.dart';
import 'package:provider/provider.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    return CustomScrollView(
      slivers: [
        SliverAppBar(
          floating: true,
          snap: true,
          pinned: true,
          expandedHeight: 200,
          backgroundColor: Colors.white,
          title: AppText(
            text: "Ecommerce App",
            color: Colors.black,
            fontSize: 23,
          ),
        ),
        SliverToBoxAdapter(
          child: Padding(
            padding: const EdgeInsets.all(30),
            child: Column(
              children: [
                SizedBox(height: 20),
                AppButton(
                  label: "Logout",
                  onPressed: () {
                    authProvider.logout();
                    CustomNavigator.navigateTo(
                      context,
                      LoginPage(),
                      replace: true,
                    );
                  },
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}
