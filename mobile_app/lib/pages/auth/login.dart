import 'dart:convert';

import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:http/src/response.dart';
import 'package:mobile_app/pages/app_layout.dart';
import 'package:mobile_app/pages/auth/forgot_password.dart';
import 'package:mobile_app/pages/auth/register.dart';
import 'package:mobile_app/providers/auth_provider.dart';
import 'package:mobile_app/services/auth_service.dart';
import 'package:mobile_app/utils/navigator.dart';
import 'package:mobile_app/utils/snackbar.dart';
import 'package:mobile_app/widgets/app_button.dart';
import 'package:mobile_app/widgets/app_input.dart';
import 'package:mobile_app/widgets/app_text.dart';
import 'package:mobile_app/widgets/app_text_button.dart';
import 'package:provider/provider.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  final _formKey = GlobalKey<FormState>();

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    emailController.text = "admin@gmail.com";
    passwordController.text = "Admin123@@";
  }

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);

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
                      text: "Bienvenue",
                      textAlign: TextAlign.center,
                      fontSize: 25,
                    ),
                    SizedBox(height: 20),
                    AppInput(
                      label: "Adresse email",
                      controller: emailController,
                      keyboardType: TextInputType.emailAddress,
                      autofillHints: [AutofillHints.email],
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
                    AppInput(
                      label: "Mot de passe",
                      controller: passwordController,
                      validator: (password) {
                        if (password == null || password.isEmpty) {
                          return "Veuillez saisir votre mot de passe";
                        } else if (password.length < 8) {
                          return "Le mot de passe doit contenir au moins 8 caractères";
                        }
                        return null;
                      },
                      obscureText: true,
                      prefixIcon: Icons.lock,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        AppTextButton(
                          label: "Mot de passe oublié ?",
                          onPressed: () {
                            CustomNavigator.navigateTo(
                              context,
                              const ForgotPasswordPage(),
                            );
                          },
                        ),
                      ],
                    ),
                    SizedBox(height: 10),
                    AppButton(
                      onPressed: () async {
                        if (!_formKey.currentState!.validate()) return;
                        Response response = await AuthService.login(
                          email: emailController.text,
                          password: passwordController.text,
                        );

                        if (response.statusCode == 200) {
                          final data = jsonDecode(response.body);
                          final token = data['token'];

                          await authProvider.saveToken(token);
                          CustomNavigator.navigateTo(
                            context,
                            AppLayout(),
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
                      label: "Se connecter",
                    ),
                    SizedBox(height: 20),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        AppText(text: "Vous n'avez pas de compte ?"),
                        AppTextButton(
                          onPressed: () {
                            WidgetsBinding.instance.addPostFrameCallback((_) {
                              CustomNavigator.navigateTo(
                                context,
                                RegisterPage(),
                              );
                            });
                          },
                          label: "S'inscrire",
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
