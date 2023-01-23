package de.ideaonic703.gd.engine.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderProgramID;
    private String vertexSource, fragmentSource, filepath;
    private boolean beingUsed = false;
    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            int index = source.indexOf("#type")+6;
            int eol = source.indexOf('\n', index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol)+6;
            eol = source.indexOf('\n', index);
            String secondPattern = source.substring(index, eol).trim();
            if(firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if(firstPattern.equalsIgnoreCase("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }
            if(secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if(secondPattern.equalsIgnoreCase("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        } catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }
    public void compile() {
        int vertexID, fragmentID;
// Compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        if(glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '"+filepath+"'\n\tVertex shader compilation failed.'");
            System.err.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }
        // Compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        if(glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '"+filepath+"'\n\tFragment shader compilation failed.'");
            System.err.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }
        // Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        if(glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '"+filepath+"'\n\tLinking shaders failed.'");
            System.err.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }
    public void use() {
        if(beingUsed) return;
        glUseProgram(shaderProgramID);
        beingUsed = true;
    }
    public void detach() {
        if(!beingUsed) return;
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        val.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
    public void uploadMat3f(String varName, Matrix3f val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        val.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }
    public void uploadMat2f(String varName, Matrix2f val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
        val.get(matBuffer);
        glUniformMatrix2fv(varLocation, false, matBuffer);
    }
    public void uploadVec4f(String varName, Vector4f val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform4f(varLocation, val.x, val.y, val.z, val.w);
    }
    public void uploadVec3f(String varName, Vector3f val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform3f(varLocation, val.x, val.y, val.z);
    }
    public void uploadVec2f(String varName, Vector2f val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform2f(varLocation, val.x, val.y);
    }
    public void uploadFloat(String varName, float val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1f(varLocation, val);
    }
    public void uploadInt(String varName, int val) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, val);
    }
    public void uploadTexture(String varName, int slot) {
        use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, slot);
    }
}
