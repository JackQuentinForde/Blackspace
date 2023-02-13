package projectBlackSpaceApp;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shaders {

    //Shaders:
    private String vertexShaderSource = "#version 120\n" +
            "attribute vec3 in_position;\n" +
            "\nvoid main() {\n" +
            "\ngl_Position = vec4(in_position, 1.0);" +
            "}";

    private String fragmentShaderSource = "#version 120\n" +
            "\nuniform vec3 colourMod;" +
            "\nvoid main() {\n" +
            "gl_FragColor = vec4(colourMod, 1.0f);\n" +
            "}";

    private int vertexShader;
    private int fragmentShader;
    protected int shaderProgram;


    protected void init() {

        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS)==GL_FALSE) {
            throw new IllegalStateException("Vertex shader failed to compile\n" +
                    glGetShaderInfoLog(vertexShader, glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH)));
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS)==GL_FALSE) {
            throw new IllegalStateException("Fragment shader failed to compile\n" +
                    glGetShaderInfoLog(fragmentShader, glGetShaderi(fragmentShader, GL_INFO_LOG_LENGTH)));
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glBindAttribLocation(shaderProgram, 0, "in_position");
        glLinkProgram(shaderProgram);
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS)==GL_FALSE) {
            throw new IllegalStateException("Program linking was unsuccessful\n" +
                    glGetProgramInfoLog(shaderProgram, glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)));
        }

        //Shader objects are no longer needed
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    protected void useProgram() {
        glUseProgram(shaderProgram);
    }

    protected void deleteProgram() {
        glDeleteProgram(shaderProgram);
    }
}
