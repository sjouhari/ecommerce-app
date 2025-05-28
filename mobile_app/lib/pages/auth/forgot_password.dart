import 'dart:convert';

import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:http/src/response.dart';
import 'package:mobile_app/pages/auth/login.dart';
import 'package:mobile_app/services/auth_service.dart';
import 'package:mobile_app/utils/navigator.dart';
import 'package:mobile_app/utils/snackbar.dart';
import 'package:mobile_app/widgets/app_button.dart';
import 'package:mobile_app/widgets/app_input.dart';
import 'package:mobile_app/widgets/app_text.dart';
import 'package:mobile_app/widgets/app_text_button.dart';

class ForgotPasswordPage extends StatefulWidget {
  const ForgotPasswordPage({super.key});

  @override
  State<ForgotPasswordPage> createState() => _ForgotPasswordPageState();
}

class _ForgotPasswordPageState extends State<ForgotPasswordPage> {
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: Center(
          // centre horizontalement
          child: SingleChildScrollView(
            // permet scroll si écran trop petit
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: 30),
              child: Form(
                key: _formKey,
                child: Column(
                  mainAxisSize:
                      MainAxisSize.min, // prend juste la place nécessaire
                  mainAxisAlignment:
                      MainAxisAlignment.center, // centre verticalement
                  children: [
                    Image.asset("assets/images/logo/app-logo.png", height: 50),
                    SizedBox(height: 20),
                    AppText(
                      text: "Réinitialiser le mot de passe",
                      textAlign: TextAlign.center,
                      fontSize: 22,
                    ),
                    SizedBox(height: 20),
                    AppText(
                      text:
                          "Vous allez recevoir un lien par mail pour changer votre mot de passe",
                      textAlign: TextAlign.center,
                      fontSize: 18,
                    ),
                    SizedBox(height: 20),
                    AppInput(
                      label: "Adresse email",
                      controller: emailController,
                      validator: (email) {
                        if (email == null || email.isEmpty) {
                          return "Veuillez saisir votre adresse email";
                        }
                        if (!EmailValidator.validate(email)) {
                          return "Veuillez saisir une adresse email valide";
                        }
                        return null;
                      },
                      prefixIcon: Icons.email,
                    ),
                    SizedBox(height: 20),
                    AppButton(
                      onPressed: () async {
                        if (!_formKey.currentState!.validate()) return;
                        Response response = await AuthService.forgotPassword(
                          email: emailController.text,
                        );

                        if (response.statusCode == 200) {
                          final data = jsonDecode(response.body);
                          final message = data['message'];

                          showCustomSnackBar(context, message);
                          CustomNavigator.navigateTo(
                            context,
                            LoginPage(),
                            replace: true,
                          );
                        } else if (response.statusCode == 500) {
                          final data = jsonDecode(response.body);
                          final message = data['message'];

                          showCustomSnackBar(context, message, isError: true);
                        } else {
                          showCustomSnackBar(
                            context,
                            "Une erreur s'est produite",
                            isError: true,
                          );
                        }
                      },
                      label: "Envoyer",
                    ),
                    SizedBox(height: 20),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        AppTextButton(
                          onPressed: () {
                            WidgetsBinding.instance.addPostFrameCallback((_) {
                              CustomNavigator.navigateTo(context, LoginPage());
                            });
                          },
                          label: "Se connecter",
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
