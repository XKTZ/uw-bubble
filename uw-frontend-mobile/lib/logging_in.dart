import 'package:flutter/material.dart';

class LoggingIn extends StatefulWidget{
  const LoggingIn({Key? key}) : super(key: key);

  @override
  State<LoggingIn> createState() => _LoggingInState();
}

class _LoggingInState extends State<LoggingIn>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.blueAccent,
      body: SafeArea(
        child: Column(children: [
          Align(
            alignment: Alignment.topLeft,
            child: BackButton(
              onPressed: () {
                Navigator.pop(context);
              },
            ),
          ),
          // Spacing
          SizedBox(height: 300),

          // Logo
          Center(
            child: Text(
              'uwbubbles',
              style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 40,
              ),
            ),
          ),

          // Email Text Field
          SizedBox(height: 80),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 25),
            child: Container(
              decoration: BoxDecoration(
                color: Colors.lightBlue,
                boxShadow: [
                  BoxShadow(
                    color: Colors.indigo.withOpacity(0.5),
                    spreadRadius: 5,
                    blurRadius: 7,
                    offset: Offset(0, 3), // changes position of shadow
                  ),
                ],
                // border: Border.all(color: Colors.black),
                borderRadius: BorderRadius.circular(14),
              ),
              child: Padding(
                padding: const EdgeInsets.only(left: 20),
                child: TextField(
                  decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Your Username',
                  ),
                ),
              ),
            ),
          ),

          // Password Text Field
          SizedBox(height: 15),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 25),
            child: Container(
              decoration: BoxDecoration(
                color: Colors.lightBlue,
                boxShadow: [
                  BoxShadow(
                    color: Colors.indigo.withOpacity(0.5),
                    spreadRadius: 5,
                    blurRadius: 7,
                    offset: Offset(0, 3), // changes position of shadow
                  ),
                ],
                borderRadius: BorderRadius.circular(14),
              ),
              child: Padding(
                padding: const EdgeInsets.only(left: 20),
                child: TextField(
                  obscureText: true,
                  decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Password',
                  ),
                ),
              ),
            ),
          ),

          // Button
          SizedBox(height: 15),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 85),
            child: TextButton(
              onPressed: () {

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
        ]),
      ),
    );
  }
}