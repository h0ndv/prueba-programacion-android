# Android Task Manager

A simple Android application for managing tasks with add and delete functionality.

## 📱 Features

- **User Login**: Login screen with username and password fields
- **Task Management**: 
  - Add new tasks with name and description
  - Delete selected tasks
  - Visual list of all tasks
- **Intuitive Interface**: Modern design with ConstraintLayout

## 🛠️ Technologies Used

- **Language**: Java
- **Platform**: Android
- **UI Framework**: ConstraintLayout
- **IDE**: Android Studio
- **Build System**: Gradle

## 📋 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/pruebaandroid/
│   │   ├── MainActivity.java          # Main activity (Login)
│   │   ├── SecondActivity.java        # Task management activity
│   │   └── Task.java                  # Data model for tasks
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml      # Login layout
│   │   │   └── activity_second.xml    # Task management layout
│   │   └── values/
│   │       └── strings.xml            # Text resources
│   └── AndroidManifest.xml
└── build.gradle
```

## 🚀 Installation and Setup

### Prerequisites

- Android Studio (latest version)
- Android SDK API 21 or higher
- Java 8 or higher

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/pruebaandroid.git
   cd pruebaandroid
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory and select it

3. **Sync Gradle**
   - Android Studio will automatically sync dependencies
   - If there are issues, click "Sync Project with Gradle Files"

4. **Run the application**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

## 📖 Usage

### Login Screen
1. Enter your username and password
2. Press "Iniciar Sesion" to access task management

### Task Management
1. **Add Task**:
   - Enter the task name
   - Enter the description
   - Press "Añadir Tarea"

2. **Delete Task**:
   - Select a task from the list by tapping it
   - Press "Eliminar Tarea"
   - The task will be removed from the list

## 🔧 Technical Features

### Data Model
- **Task.java**: Class representing a task with:
  - Unique ID
  - Task name
  - Description

### Validations
- Text fields cannot be empty
- Selection validation before deletion
- Error handling with informative messages

### UI/UX
- Responsive design with ConstraintLayout
- Consistent margins and spacing
- Visual feedback with Toast messages

## 🤝 Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
