# Variables
JAVAC := javac
JAVA := java

BIN_DIR := bin
LIB_DIR := lib
MAIN_CLASS := FeedReaderMain	#SOLO CAMBIAR ESTA LINEA POR EL NOMBRE DEL ARCHIVO

# Configuración del classpath
CLASSPATH := $(LIB_DIR)/*:$(BIN_DIR)

# Encontrar todos los archivos .java recursivamente desde el directorio padre
# (asumiendo que el Makefile está en src/_test_/)
SOURCE_DIR := src
SOURCES := $(shell find $(SOURCE_DIR) -type f -name "*.java")

# Reglas
.PHONY: all compile run clean

all: compile run

compile:
	@echo "Compilando fuentes..."
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -nowarn -Xlint:-deprecation -cp "$(CLASSPATH)" -d $(BIN_DIR) $(SOURCES)
	@echo "Compilación completada."

run:
	@echo "Ejecutando programa..."
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN_CLASS)

ne:
	@echo "Compilando fuentes..."
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -nowarn -Xlint:-deprecation -cp "$(CLASSPATH)" -d $(BIN_DIR) $(SOURCES)
	@echo "Ejecutando programa con NE..."
	$(JAVA) -cp "$(CLASSPATH)" $(MAIN_CLASS) -ne

clean:
	@echo "Limpiando archivos compilados..."
	@rm -rf $(BIN_DIR)
	@echo "Limpieza completada."# Variables
