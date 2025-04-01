CREATE TABLE CentroEstudios (
                                CentroID INT AUTO_INCREMENT PRIMARY KEY,
                                Nombre NVARCHAR(255) NOT NULL
);

CREATE TABLE Usuarios (
                          UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
                          CentroID INT NOT NULL,
                          Nombre NVARCHAR(255) NOT NULL,
                          Email NVARCHAR(255) UNIQUE NOT NULL,
                          Contrasena NVARCHAR(255) NOT NULL,
                          AlmacenamientoDisponible INT NOT NULL,
                          ArchivosGuardados INT DEFAULT 0,
                          CONSTRAINT FK_Usuarios_CentroEstudios FOREIGN KEY (CentroID)
                              REFERENCES CentroEstudios(CentroID)
                              ON DELETE CASCADE
);

CREATE TABLE Archivos (
                          ArchivoID INT AUTO_INCREMENT PRIMARY KEY,
                          Nombre VARCHAR(255),
                          UsuarioID INT NOT NULL,
                          Fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          Contenido NVARCHAR(MAX) NOT NULL,
                          Tamano INT,
                          CONSTRAINT FK_Archivos_Usuarios FOREIGN KEY (UsuarioID)
                              REFERENCES Usuarios(UsuarioID)
                              ON DELETE CASCADE
);

