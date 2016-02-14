# !/bin/bash

echo "Pasta atual:"
pwd

echo "Compilando o programa"
javac *.java

echo "Compilado com sucesso"

echo "Subindo uma pasta"
cd ../

echo "Pasta atual:"
pwd

echo "Para alterar as entradas modifique o arquivo entrada.txt"

echo "Executaremos o programa"
java gerenciamento.Principal < entrada.txt
