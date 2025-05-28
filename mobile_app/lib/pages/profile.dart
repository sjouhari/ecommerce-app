import 'package:flutter/material.dart';
import 'package:mobile_app/widgets/app_text.dart';

class ProfilePage extends StatefulWidget {
  const ProfilePage({super.key});

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  @override
  Widget build(BuildContext context) {
    return Center(child: AppText(text: "Profile"));
  }
}
