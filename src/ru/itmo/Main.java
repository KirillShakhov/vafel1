package ru.itmo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.abs;

public class Main {
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("1. Ввод с клавиатуры\n2. Ввод с файла");
        String line = scanner.nextLine();
        if (line.equals("1")) {
            System.out.println("Вводим матрицу с консоли");
            double[][] matrix = createMatrixFromKeyBoard();
            double eps;
            while (true) {
                try {
                    String buffer = scanner.nextLine();
                    eps = Double.parseDouble(buffer);
                    break;
                } catch (Exception ignored) {
                }
            }
            findSolution(matrix, eps);
        } else if (line.equals("2")) {
            System.out.println("Вводим матрицу с файла");
            System.out.println("Имя файла:");
            String path = scanner.nextLine();
            double[][] matrix = readMatrixFromFile(path);
            double eps;
            while (true) {
                try {
                    System.out.println("Введите точность:");
                    String buffer = scanner.nextLine();
                    eps = Double.parseDouble(buffer);
                    break;
                } catch (Exception ignored) {
                }
            }
            findSolution(matrix, eps);
        }
    }

    public static void findSolution(double[][] matrix, double eps) {
        if (checkDiagonal(matrix, matrix.length)) {
            solve(matrix, eps);
            return;
        }
        double[][] val = permuteMatrixHelper(matrix, 0);
        if (val != null) {
            solve(val, eps);
        } else {
            System.out.println("Диагонального преобладание не удалось достичь");
        }
    }

    public static boolean checkDiagonal(double[][] matrix, int size) {
        int i, j, k = 1;
        double sum = 0;
        for (i = 0; i < size; i++) {
            sum = 0;
            for (j = 0; j < size; j++) {
                sum += abs(matrix[i][j]);
            }
            sum -= abs(matrix[i][i]);
            if (sum >= abs(matrix[i][i])) {
                k = 0;
            }
        }
        return (k == 1);
    }

    public static double[][] permuteMatrixHelper(double[][] matrix, int index) {
        double[][] val = null;
        if(index >= matrix.length - 1){
            if (checkDiagonal(matrix, matrix.length)){
                val = new double[matrix.length][matrix.length+1];
                for (int i = 0; i < matrix.length;i++){
                    for (int j = 0; j < matrix.length+1;j++){
                        val[i][j] = matrix[i][j];
                    }
                }
            }
            return val;
        } else {
            for (int i = index; i < matrix.length; i++) {
                double[] t = matrix[index];
                matrix[index] = matrix[i];
                matrix[i] = t;

                permuteMatrixHelper(matrix, index + 1);

                t = matrix[index];
                matrix[index] = matrix[i];
                matrix[i] = t;
            }
        }
        return val;
    }
    private static void solve(double[][] matrix, double eps) {
        double[] x= new double[matrix.length];
        double norma = 0, sum, t;
        do
        {
            ArrayList<Double> es = new ArrayList<>();
            norma = 0;
            //  k++;
            for(int i = 0; i < matrix.length; i++)
            {
                t = x[i];
                sum = 0;

                for(int j = 0; j < matrix.length; j++)
                {
                    if(j != i)
                        sum += matrix[i][j] * x[j];
                }
                x[i] = (getVector(matrix)[i] - sum) / matrix[i][i];
                es.add(abs(x[i] - t));
                if (abs(x[i] - t) > norma)
                    norma = abs(x[i] - t);
            }
        }
        while(norma > eps);
        System.out.println("Результат");
        for(int i = 0; i < x.length; i++){ System.out.println("x"+(i+1)+" = "+String.format("%.6f",x[i])); }
        System.out.println("Вектор невязки:");
        ArrayList<Double> residuals = new ArrayList<>();
        for(int i = 0; i < matrix.length; i++)
        {
            float S=0;
            for(int j = 0; j < matrix.length; j++)
            {
                S += matrix[i][j] * x[j] ;
            }
            System.out.println("del x"+(i+1)+" = "+String.format("%.6f",S - getVector(matrix)[i]));
        }
    }
    public static double[] getVector(double[][] matrix){
        double [] vector = new double[matrix.length];
        for(int i = 0; i < matrix.length; i++){
            vector[i]=matrix[i][matrix.length];
        }
        return vector;
    }
    public static double[][] createMatrixFromKeyBoard(){
        try {
            System.out.println("Введите размерность матрицы");
            String buffer = scanner.nextLine();
            buffer = buffer.trim();
            int size = Integer.parseInt(buffer);
            if (size > 20 || size <= 0) {
                throw new Exception();
            }
            System.out.println("Введите строки матрицы");
            double [][] matrix = new double[size][size+1];
            String [][] arr = new String[size][size+1];
            for (int i = 0; i < size;i++) {
                buffer = scanner.nextLine();
                arr[i] = buffer.trim().split(" ");
                buffer = "";
            }
            for (int i = 0; i < size;i++){
                for (int j = 0; j < size+1;j++) {
                    matrix[i][j] = Double.parseDouble(arr[i][j].trim());
                }
            }
            return matrix;
        } catch (Exception e) {
            System.out.println("Введена неверная размерность");
        }
        return null;
    }

    public static double[][] readMatrixFromFile(String fileName) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(new File(fileName)));
            int size = Integer.parseInt(file.readLine().trim());
            double [][] matrix = new double[size][size + 1];
            for (int i = 0; i < size; i++) {
                String[] row = file.readLine().trim().split(" ");
                if (row.length > size + 1)
                    throw new ArrayIndexOutOfBoundsException();
                for (int j = 0; j < size + 1; j++) {
                    matrix[i][j] = Double.parseDouble(row[j].trim());
                }
            }
            return matrix;
        } catch (IOException e) {
            System.out.println("Ошибка ввода");
        }
        return null;
    }
}
