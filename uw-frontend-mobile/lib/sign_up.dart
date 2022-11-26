import 'package:flutter/material.dart';
import 'sign_up2.dart';

class SignUp extends StatefulWidget{
  const SignUp({Key? key}) : super(key: key);

  @override
  State<SignUp> createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
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
          // Text
          SizedBox(height: 50),
          Padding(
            padding: const EdgeInsets.only(left: 35),
            child: Center(
              child: Text(
                'We\'re so excited for you to join us!',
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 30,
                ),
              ),
            ),
          ),

          // Icon + create an account
          SizedBox(height: 80),
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.only(left: 35),
                child: Icon(
                  Icons.looks_one_rounded,
                  color: Colors.indigo[900],
                  size: 40,
                  semanticLabel: 'Create an account',
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(left: 30),
                child: Text(
                  'Create an account',
                  style: TextStyle(
                    fontSize: 25,
                    fontWeight: FontWeight.bold,
                  )
                ),
              )
            ],
          ),

          // Text Fields
          SizedBox(height: 40),
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
                    hintText: 'Your Waterloo Email',
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
                // border: Border.all(color: Colors.black),
                borderRadius: BorderRadius.circular(14),
              ),
              child: Padding(
                padding: const EdgeInsets.only(left: 20),
                child: TextField(
                  obscureText: true,
                  decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Create a Password',
                  ),
                ),
              ),
            ),
          ),

          // Create Account
          SizedBox(height: 15),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 85),
            child: TextButton(
              onPressed: () {
                // will add feature
                Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const SignUp2()),
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
                padding: EdgeInsets.all(10),
                decoration: BoxDecoration(
                    color: Colors.deepPurple[600],
                    borderRadius: BorderRadius.circular(16)
                ),
                child: Center(
                  child: Text(
                    'Create Account',
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

          // Steps indicator
          SizedBox(height: 200),
          Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(
                Icons.circle_sharp,
                color: Colors.orange[900],
                size: 40,
              ),
              Icon(
                Icons.circle_outlined,
                color: Colors.black,
                size: 40,
              ),
              Icon(
                Icons.circle_outlined,
                color: Colors.black,
                size: 40,
              ),
            ],
          ),

        ],
        ),
      ),
    );
  }
}