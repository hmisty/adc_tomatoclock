ADC
===
ADC, Android Development Course, is a series of Android development classes taught by Evan Liu (hmisty) in Beihang University Software College, Beijing, China.

ADC Tomato Clock
---
ADC Tomato Clock is the 6th homework of the ADC.
The source code here is a partially finished Android application. The goal for the students is to complete the application as required.

Homework
===

Requirements
---
There are three requirements of the homework:

### Fix the bug that bounded service will be killed while switching between landscape and portrait views
When rotating the phone from portrait to landscape or vice versa, the activity will be destroyed and recreated. 
Meanwhile, the bounded service will be killed too. 
Therefore, the timer will stop ticking on the screen. 
Please fix the bug and keep the timer ticking while rotating the phone.

### Fix the bug that the timer will tick incorrectly if clicking the tomato more than once
Click the tomato, the timer will start ticking.
Click again, the timer will run incorrectly.
Please fix it to let the timer restart from 00:00 if clicking again during the ticking period.

### Use foreground service
Change the service to foreground and show the status bar notification.
Reference: http://developer.android.com/guide/topics/fundamentals/services.html#Foreground

Submission
---
  * Source codes
  * Document
  * Screenshots

HowTo
---
Fork, complete, commit, push and send me a pull request at hmisty/adc_tomatoclock.

The MIT License
---
Copyright (c) 2012
Evan (Qingyan) Liu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
