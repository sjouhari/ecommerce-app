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

class RegisterPage extends StatefulWidget {
  const RegisterPage({super.key});

  @override
  State<RegisterPage> createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  TextEditingController firstNameController = TextEditingController();
  TextEditingController lastNameController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: GestureDetector(
        onTap: () {
          FocusScope.of(context).unfocus();
        },
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
                      Image.asset(
                        "assets/images/logo/app-logo.png",
                        height: 50,
                      ),
                      SizedBox(height: 20),
                      AppText(
                        text: "Créer un compte",
                        textAlign: TextAlign.center,
                        fontSize: 25,
                      ),
                      SizedBox(height: 20),
                      AppInput(
                        label: "Nom",
                        controller: lastNameController,
                        validator: (lastName) {
                          if (lastName == null || lastName.isEmpty) {
                            return "Veuillez saisir votre nom";
                          }
                          return null;
                        },
                        prefixIcon: Icons.person_2,
                      ),
                      SizedBox(height: 20),
                      AppInput(
                        label: "Prénom",
                        controller: firstNameController,
                        validator: (firstName) {
                          if (firstName == null || firstName.isEmpty) {
                            return "Veuillez saisir votre prénom";
                          }
                          return null;
                        },
                        prefixIcon: Icons.person_3,
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
                      SizedBox(height: 20),
                      AppButton(
                        onPressed: () async {
                          if (!_formKey.currentState!.validate()) return;
                          Response response = await AuthService.register(
                            firstName: firstNameController.text,
                            lastName: lastNameController.text,
                            email: emailController.text,
                            password: passwordController.text,
                          );

                          if (response.statusCode == 201) {
                            final data = jsonDecode(response.body);
                            print(data);
                            showCustomSnackBar(context, data['message']);
                            CustomNavigator.navigateTo(context, LoginPage());
                          } else if (response.statusCode == 500) {
                            final data = jsonDecode(response.body);
                            final message = data['message'];

                            showCustomSnackBar(context, message, isError: true);
                          } else {
                            showCustomSnackBar(
                              context,
                              "Une erreur s'est produite${response.body}",
                              isError: true,
                            );
                          }
                        },
                        label: "S'inscrire",
                      ),
                      SizedBox(height: 20),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          AppText(
                            text: "Vous avez deja un compte ?",
                            fontSize: 20,
                          ),
                          AppTextButton(
                            onPressed: () {
                              CustomNavigator.goBack(context);
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
      ),
    );
  }
}
