import 'package:flutter/material.dart';

class SignUp2 extends StatefulWidget{
  const SignUp2({Key? key}) : super(key: key);

  @override
  State<SignUp2> createState() => _SignUp2State();
}

class _SignUp2State extends State<SignUp2> {
  List<String> faculty = ['None', 'Arts', 'Engineering', 'Environment',
  'Health', 'Mathematics', 'Science'];
  String? selectedFaculty = 'None';

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
          // Icon + create an account
          SizedBox(height: 20),
          Row(
            children: [
              Padding(
                padding: const EdgeInsets.only(left: 35),
                child: Icon(
                  Icons.looks_two_rounded,
                  color: Colors.indigo[900],
                  size: 40,
                  semanticLabel: 'Tell us a bit about yourself',
                ),
              ),
              Expanded(
                child: Padding(
                  padding: const EdgeInsets.only(left: 30),
                  child: Text(
                      'Tell us a bit about yourself',
                      style: TextStyle(
                        fontSize: 25,
                        fontWeight: FontWeight.bold,
                      ),
                  ),
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
                    hintText: 'Your Name',
                  ),
                ),
              ),
            ),
          ),

          // Password Text Field
          SizedBox(height: 15),
          DropdownButton<String>(
            value: selectedFaculty,
            items: faculty.map((faculty) => DropdownMenuItem<String>(
              value: faculty,
              child: Text(faculty, style: TextStyle(fontSize: 24)),)).toList(),
            onChanged: (faculty) => setState(() => selectedFaculty = faculty),
            dropdownColor: Colors.white,
          ),

          // Create Account
          SizedBox(height: 15),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 85),
            child: TextButton(
              onPressed: () {
                // will add feature
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
                Icons.circle_sharp,
                color: Colors.orange[900],
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