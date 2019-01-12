## 2019 数字图像处理 课程设计

### 一个图像浮雕和图像油画算法的设计和实现

#### 编译代码

本程序包分为三个部分，需要分别编译并整合到一起才能看到完整效果

- web前端：在`src/main/react-view`下执行`npm install`和`npm run build`
- web后台（包括浮雕算法）：在源代码根目录执行`gradle bootJar`，执行后在`build/libs`下可得到一个`jar`文件，此时web前端已打包进去，所以需要先编译前端再编译Java后台
- 油画算法：在`src/paint.cpp`，需要有opencv开发环境进行编译，编译后得到一个可执行文件，需要命名为`paint`或`paint.exe`（windows）

#### 运行时依赖

- Java后台：jar文件需要依赖于`opencv4.0.1`的动态链接库，windows下为`opencv_java401.dll`，其他平台为`libopencv_java401.so`，需要放在jar文件目录的opencv目录下
- 油画算法模块：需要使用编译`src/paint.cpp`时使用的对应opencv版本的动态运行时链接库，一般为`opencv_worldxxx.dll`，其中xxx是版本号。编译后的可执行文件和动态链接库都必须放在jar文件的同目录下，jar文件在运行时会调用该文件
- web前端依赖于浏览器，请使用现代主流浏览器

#### 可执行程序包

程序使用`opencv`进行图像处理，windows版本可执行文件可在 [Realease](https://github.com/x-insane/2019-graphic-final/releases) 中下载，如无法运行，需要在相应的环境重新编译源代码。

#### 运行程序

- JAR文件是程序主体，使用方法为`java -jar <jar_file_name> [<port>]`，端口号为可选，默认为1963
- 成功运行后，可打开浏览器访问`http://localhost:1963`或对应IP及端口号