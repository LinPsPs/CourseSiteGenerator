# CourseSiteGenerator
## Introduction
This is my CSE 219 course project. The **Course Site Generator** application intends to automate the process of building and updating a course Web.

The Web page produced by this app will be well-organized and will be able to customize in many different ways but will exist within a common site template.

The details of this project are specified in "CourseSiteGeneratorSDD.pdf" and "CourseSiteGeneratorSRS.pdf".

## Core Features
1. This app provides a user-friendly UI that allows users to easily generate websites. This app supports undo/redo and save/load operations. The UI is presented by JavaFX Framework. The undo/redo operations are provided by jTPS - An abstract transaction processing system. The data inputted by users will be stored in JSON files.
2. This app allows users to change themes for both app UI and Web page. Simply put CSS files under webpage_css folder, the app will load from this folder and allow users to select themes from a menu.
3. This app also supports multiple languages. It uses PropertyManager to load XML files which stores languages.


![alt test](https://raw.githubusercontent.com/LinPsPs/CourseSiteGenerator/master/WelDialog.png "WelDialog.png")
![alt test](https://raw.githubusercontent.com/LinPsPs/CourseSiteGenerator/master/CourseSiteGenMainPage.png "MainPage.png")
![alt test](https://raw.githubusercontent.com/LinPsPs/CourseSiteGenerator/master/CourseScheduleTab.png "CourseScheduleTab.png")
![alt test](https://raw.githubusercontent.com/LinPsPs/CourseSiteGenerator/master/OHPage.png "OHPage.png")

**Respect to Academic Integrity. If you are a SBU student taking CSE219, do not clone this project and submit as your own final project! This project only used for learning**
