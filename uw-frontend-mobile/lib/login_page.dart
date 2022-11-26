import 'package:flutter/material.dart';
import 'logging_in.dart';
import 'sign_up.dart';

class LoginPage extends StatefulWidget{
  const LoginPage({Key? key}) : super(key: key);

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blueAccent,
      body: SafeArea(
        child: Column(children: [
          SizedBox(height: 10),
          // UW bubbles
          Padding(
            padding: const EdgeInsets.all(10),
            child: Align(
              alignment: Alignment.topLeft,
              child: Text(
                'uwbubbles',
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 30,
                ),
              ),
            ),
          ),

          // bubble pngs
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.all(25),
                child: Image.asset('assets/images/1bubble.png',
                scale: 6),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 35),
                child: Image.asset('assets/images/2bubble.png',
                scale: 8),
              ),
            ],
          ),
          // Networking has...
          SizedBox(height: 120),
          Text(
            'Networking has never been easier',
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 24,
            ),
          ),
          // Log in button
          SizedBox(height: 50),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 85),
            child: TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const LoggingIn()),
                );
              },
              style: ButtonStyle(
                overlayColor: MaterialStateProperty.resolveWith<Color?>(
                      (Set<MaterialState> states) {
                    if (states.contains(MaterialState.focused) ||
                        states.contains(MaterialState.pressed))
                      return Colors.black12.withOpacity(0.12);
                    return null; // Defer to the widget's default.
                  },
                ),
              ),
              child: Container(
                padding: EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Colors.deepPurple[600],
                  borderRadius: BorderRadius.circular(16)
                ),
                child: Center(
                  child: Text(
                    'Log in',
                    style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                      fontSize: 25
                    ),
                  ),
                ),
              ),
            ),
          ),
          // Sign up button
          SizedBox(height: 15),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 85),
            child: TextButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const SignUp()),
                );
              },
              style: ButtonStyle(
                overlayColor: MaterialStateProperty.resolveWith<Color?>(
                      (Set<MaterialState> states) {
                    if (states.contains(MaterialState.focused) ||
                        states.contains(MaterialState.pressed))
                      return Colors.black12.withOpacity(0.12);
                    return null; // Defer to the widget's default.
                  },
                ),
              ),
              child: Container(
                padding: EdgeInsets.all(20),
                decoration: BoxDecoration(
                    color: Colors.deepPurple[600],
                    borderRadius: BorderRadius.circular(16)
                ),
                child: Center(
                  child: Text(
                      'Sign up',
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        fontSize: 25,
                      ),
                  ),
                ),
              ),
            ),
          ),
        ]),
      ),
    );
  }
}