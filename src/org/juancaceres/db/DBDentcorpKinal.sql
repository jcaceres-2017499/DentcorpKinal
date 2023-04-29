/*
    Nombre: Juan Pablo Caceres Enriquez
    Codigo Técnico: IN5BM
    Carné: 2017499
    Fecha de creación: 19-07-2022
 */
Drop database if exists DBDentcorpKinal;
Create database DBDentcorpKinal;
Use DBDentcorpKinal;

Create table Pacientes(
	codigoPaciente int not null,
    nombresPaciente varchar(50) not null,
    apellidosPaciente varchar(50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar(100) not null,
    telefonoPersonal varchar(8) not null,
    fechaPrimeraVisita date,
    primary key PK_codigoPaciente (codigoPaciente)
);

Create table Especialidades(
	codigoEspecialidad int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoEspecialidad (codigoEspecialidad)
);

Create table Medicamentos(
	codigoMedicamento int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoMedicamento (codigoMedicamento)
);

Create table Doctores(
	numeroColegiado int not null,
    nombresDoctor varchar(50) not null,
    apellidosDoctor varchar(50) not null,
    telefonoContacto varchar(8) not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades (codigoEspecialidad)
);

Create table Recetas(
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

Create table DetalleReceta(
	codigoDetalleReceta int not null auto_increment,
    dosis varchar(100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Recetas foreign key (codigoReceta)
		references Recetas (codigoReceta),
    constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos (codigoMedicamento)
);

Create table Citas(
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita varchar(150),
    tratamiento varchar(150) not null,
    desripCondActual varchar(150) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Pacientes foreign key (codigoPaciente)
		references Pacientes (codigoPaciente),
    constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

-- -----------Procedimientos Almacenados----------------
-- -----------Pacientes-----------------
-- -----------Agregar-------------------
Delimiter $$
	create procedure sp_AgregarPaciente(in codigoPaciente int, in nombresPaciente varchar(50), 
    in apellidosPaciente varchar(50), in sexo char, in fechaNacimiento date, 
    in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
        Begin
        insert into Pacientes(codigoPaciente, nombresPaciente, apellidosPaciente, sexo, 
        fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita)
			values(codigoPaciente, nombresPaciente, apellidosPaciente, upper(sexo), fechaNacimiento, 
            direccionPaciente, telefonoPersonal, fechaPrimeraVisita);
    end$$    
Delimiter ;
Call sp_AgregarPaciente(7474,'Juan Pablo','Caceres Enriquez','m','2002-10-10','Zona 7 Guatemala','47744774',now())
Call sp_AgregarPaciente(5454,'Pedro Pablo','Manuel Carrillo','m','2004-10-10','Zona 8 Guatemala','48843374',now())

-- -----------Listar-------------------
Delimiter $$
	create procedure sp_ListarPacientes()
		Begin
			Select
            p.codigoPaciente,
            p.nombresPaciente,
            p.apellidosPaciente,
            p.sexo,
            p.fechaNacimiento,
            p.direccionPaciente,
            p.telefonoPersonal,
            p.fechaPrimeraVisita
		from Pacientes p;
        End$$
Delimiter ;
Call sp_ListarPacientes()

-- --------Buscar-------------------
Delimiter $$
	create procedure sp_BuscarPaciente(in codPaciente int)
		Begin
			Select
            p.codigoPaciente,
            p.nombresPaciente,
            p.apellidosPaciente,
            p.sexo,
            p.fechaNacimiento,
            p.direccionPaciente,
            p.telefonoPersonal,
            p.fechaPrimeraVisita
            from Pacientes p
				where codigoPaciente = codPaciente;
        End$$
Delimiter ;
-- Call sp_BuscarPaciente()

-- ---------Eliminar-------------------
Delimiter $$
	create procedure sp_EliminarPaciente(in codPaciente int)
		begin
			Delete from Pacientes 
				where codigoPaciente = codPaciente;
        End$$
Delimiter ;
-- Call sp_EliminarPaciente()

-- --------Editar-------------------------
Delimiter $$
	create procedure sp_EditarPaciente(in codPaciente int, in nomPaciente varchar(50), 
		in apePaciente varchar(50), in sx char, in fechaNac date, in dirPaciente varchar(100), 
        in telPersonal varchar(8), in fechaPV date)
        Begin
			Update Pacientes p
				set p.nombresPaciente = nomPaciente,
					p.apellidosPaciente = apePaciente,
                    p.sexo = sx,
                    p. fechaNacimiento = fechaNac,
                    p. direccionPaciente = dirPaciente,
                    p. telefonoPersonal = telPersonal,
                    p. fechaPrimeraVisita = fechaPV
					where codigoPaciente = codPaciente;
        End$$
Delimiter ;
-- Call sp_EditarPaciente()

-- -----------Especialidades-----------------
-- -----------Agregar-------------------
Delimiter $$
	create procedure sp_AgregarEspecialidad(in descripcion varchar(100))
        Begin
        insert into Especialidades(descripcion)
			values(descripcion);
    end$$    
Delimiter ;
Call sp_AgregarEspecialidad('Cardiologia')

-- -----------Listar-------------------
Delimiter $$
	create procedure sp_ListarEspecialidades()
		Begin
			Select
            e.codigoEspecialidad,
            e.descripcion
		from Especialidades e;
        End$$
Delimiter ;
Call sp_ListarEspecialidades()

-- --------Buscar-------------------
Delimiter $$
	create procedure sp_BuscarEspecialidad(in codEsp int)
		Begin
			Select
            e.codigoEspecialidad,
            e.descripcion
            from Especialidades e
				where codigoEspecialidad = codEsp;
        End$$
Delimiter ;
-- Call sp_BuscarEspecialidad(1011)

-- ---------Eliminar-------------------
Delimiter $$
	create procedure sp_EliminarEspecialidad(in codEsp int)
		begin
			Delete from Especialidades 
				where codigoEspecialidad = codEsp;
        End$$
Delimiter ;
-- Call sp_EliminarEspecialidad()

-- --------Editar-------------------------
Delimiter $$
	create procedure sp_EditarEspecialidad(in codEsp int, in descr varchar(100))
        Begin
			Update Especialidades e
				set e.descripcion = descr
					where codigoEspecialidad = codEsp;
        End$$
Delimiter ;
-- Call sp_EditarEspecialidad()

-- -----------Medicamentos-----------------
-- -----------Agregar-------------------
Delimiter $$
	create procedure sp_AgregarMedicamento(in descripcion varchar(100))
        Begin
        insert into Medicamentos(descripcion)
			values(descripcion);
    end$$    
Delimiter ;
Call sp_AgregarMedicamento('Biperideno')

-- -----------Listar-------------------
Delimiter $$
	create procedure sp_ListarMedicamentos()
		Begin
			Select
            m.codigoMedicamento,
            m.descripcion
		from Medicamentos m;
        End$$
Delimiter ;
Call sp_ListarMedicamentos()

-- --------Buscar-------------------
Delimiter $$
	create procedure sp_BuscarMedicamento(in codMed int)
		Begin
			Select
            m.codigoMedicamento,
            m.descripcion
            from Medicamentos m
				where codigoMedicamento = codMed;
        End$$
Delimiter ;
-- Call sp_BuscarMedicamento(1201)

-- ---------Eliminar-------------------
Delimiter $$
	create procedure sp_EliminarMedicamento(in codMed int)
		begin
			Delete from Medicamentos 
				where codigoMedicamento = codMed;
        End$$
Delimiter ;
-- Call sp_EliminarMedicamento()

-- --------Editar-------------------------
Delimiter $$
	create procedure sp_EditarMedicamento(in codMed int, in descr varchar(100))
        Begin
			Update Medicamentos m
				set m.descripcion = descr
					where codigoMedicamento= codMed;
        End$$
Delimiter ;
-- Call sp_EditarMedicamento()

-- -----------Doctores-----------------
-- -----------Agregar-------------------
Delimiter $$
	create procedure sp_AgregarDoctor(in numeroColegiado int, in nombresDoctor varchar(50), 
    in apellidosDoctor varchar(50), in telefonoContacto varchar(8), in codigoEspecialidad int)
        Begin
        insert into Doctores(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, 
				codigoEspecialidad)
			values(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, 
            codigoEspecialidad);
    end$$    
Delimiter ;
Call sp_AgregarDoctor(1,'Juan Pablo','Caceres','12345678',1)
Call sp_AgregarDoctor(2,'Peter','Parker','12432678',2)

-- -----------Listar-------------------
Delimiter $$
	create procedure sp_ListarDoctores()
		Begin
			Select
            d.numeroColegiado,
            d.nombresDoctor,
            d.apellidosDoctor,
            d.telefonoContacto,
            d.codigoEspecialidad
		from Doctores d;
        End$$
Delimiter ;
Call sp_ListarDoctores()

-- --------Buscar-------------------
Delimiter $$
	create procedure sp_BuscarDoctor(in numCol int)
		Begin
			Select
            d.numeroColegiado,
            d.nombresDoctor,
            d.apellidosDoctor,
            d.telefonoContacto,
            d.codigoEspecialidad
            from Doctores d
				where numeroColegiado = numCol;
        End$$
Delimiter ;
-- Call sp_BuscarDoctor(1201)


-- ---------Eliminar-------------------
Delimiter $$
	create procedure sp_EliminarDoctor(in numCol int)
		begin
			Delete from Doctores 
				where numeroColegiado = numCol;
        End$$
Delimiter ;
-- Call sp_EliminarDoctor()

-- --------Editar-------------------------
Delimiter $$
	create procedure sp_EditarDoctor(in numCol int, in nomDoc varchar(50), in apeDoc varchar(50), 
		in telContacto varchar(8), in codEspecialidad int)
        Begin
			Update Doctores d
				set d.nombresDoctor = nomDoc,
					d.apellidosDoctor = apeDoc,
                    d.telefonoContacto = telContacto,
                    d.codigoEspecialidad = codEspecialidad
					where numeroColegiado = numCol;
        End$$
Delimiter ;
-- Call sp_EditarDoctor()

-- -----------Recetas-----------------
-- -----------Agregar-------------------
Delimiter $$
	create procedure sp_AgregarReceta(in fechaReceta date, in numeroColegiado int)
        Begin
        insert into Recetas(fechaReceta, numeroColegiado)
			values(fechaReceta, numeroColegiado);
    end$$    
Delimiter ;
Call sp_AgregarReceta('2022-12-12',1)

-- -----------Listar-------------------
Delimiter $$
	create procedure sp_ListarRecetas()
		Begin
			Select
            r.codigoReceta,
            r.fechaReceta,
            r.numeroColegiado
		from Recetas r;
        End$$
Delimiter ;
Call sp_ListarRecetas()

-- --------Buscar-------------------
Delimiter $$
	create procedure sp_BuscarReceta(in codReceta int)
		Begin
			Select
            r.codigoReceta,
            r.fechaReceta,
            r.numeroColegiado
            from Recetas r
				where codigoReceta = codReceta;
        End$$
Delimiter ;
-- Call sp_BuscarReceta(1)

-- ---------Eliminar-------------------
Delimiter $$
	create procedure sp_EliminarReceta(in codReceta int)
		begin
			Delete from Recetas 
				where codigoReceta = codReceta;
        End$$
Delimiter ;
-- Call sp_EliminarReceta()

-- --------Editar-------------------------
Delimiter $$
	create procedure sp_EditarReceta(in codReceta int, in fechReceta date)
        Begin
			Update Recetas r
				set r.fechaReceta = fechReceta
					where codigoReceta = codReceta;
        End$$
Delimiter ;
-- Call sp_EditarReceta()

-- -----------Detalle Receta-----------------
-- -----------Agregar-------------------
Delimiter $$
	create procedure sp_AgregarDetalleReceta(in dosis varchar(100), 
		in codigoReceta int, in codigoMedicamento int)
        Begin
        insert into DetalleReceta(dosis, codigoReceta, codigoMedicamento)
			values(dosis, codigoReceta, codigoMedicamento);
    end$$    
Delimiter ;
Call sp_AgregarDetalleReceta(' 1 dosis',1,1)

-- -----------Listar-------------------
Delimiter $$
	create procedure sp_ListarDetalleRecetas()
		Begin
			Select
            t.codigoDetalleReceta,
            t.dosis,
            t.codigoReceta,
            t.codigoMedicamento
		from DetalleReceta t;
        End$$
Delimiter ;
Call sp_ListarDetalleRecetas()

-- --------Buscar-------------------
Delimiter $$
	create procedure sp_BuscarDetalleReceta(in codDR int)
		Begin
			Select
            t.codigoDetalleReceta,
            t.dosis,
            t.codigoReceta,
            t.codigoMedicamento
            from DetalleReceta t
				where codigoDetalleReceta = codDR;
        End$$
Delimiter ;
-- Call sp_BuscarDetalleReceta(1)

-- ---------Eliminar-------------------
Delimiter $$
	create procedure sp_EliminarDetalleReceta(in codDR int)
		begin
			Delete from DetalleReceta 
				where codigoDetalleReceta = codDR;
        End$$
Delimiter ;
-- Call sp_EliminarDetalleReceta()

-- --------Editar-------------------------
Delimiter $$
	create procedure sp_EditarDetalleReceta(in codDR int, in dsis varchar(100), in codReceta int, in codMedicamento int)
        Begin
			Update DetalleReceta t
				set t.dosis = dsis,
					t.codigoReceta = codReceta,
                    t.codigoMedicamento = codMedicamento
					where codigoDetalleReceta = codDR;
        End$$
Delimiter ;
-- Call sp_EditarDetalleReceta()

-- -----------Citas-----------------
-- -----------Agregar-------------------
Delimiter $$
	create procedure sp_AgregarCita(in fechaCita date, in horaCita varchar(150), 
		in tratamiento varchar(150), in desripCondActual varchar(150), in codigoPaciente int, 
        in numeroColegiado int)
        Begin
        insert into Citas(fechaCita, horaCita, tratamiento, desripCondActual, 
			codigoPaciente, numeroColegiado)
			values(fechaCita, horaCita, tratamiento, desripCondActual, codigoPaciente, 
            numeroColegiado);
    end$$    
Delimiter ;
Call sp_AgregarCita('2022-10-10','1:00','mucho','mal',7474,1)

-- -----------Listar-------------------
Delimiter $$
	create procedure sp_ListarCitas()
		Begin
			Select
            c.codigoCita,
            c.fechaCita,
            c.horaCita,
            c.tratamiento,
            c.desripCondActual,
            c.codigoPaciente,
            c.numeroColegiado
		from Citas c;
        End$$
Delimiter ;
Call sp_ListarCitas()

-- --------Buscar-------------------
Delimiter $$
	create procedure sp_BuscarCita(in codCita int)
		Begin
			Select
            c.codigoCita,
            c.fechaCita,
            c.horaCita,
            c.tratamiento,
            c.desripCondActual,
            c.codigoPaciente,
            c.numeroColegiado
            from Citas
				where codigoCita = codCita;
        End$$
Delimiter ;
-- Call sp_BuscarCita()

-- ---------Eliminar-------------------
Delimiter $$
	create procedure sp_EliminarCita(in codCita int)
		begin
			Delete from Citas 
				where codigoCita = codCita;
        End$$
Delimiter ;
-- Call sp_EliminarCita()

-- --------Editar-------------------------
Delimiter $$
	create procedure sp_EditarCita(in codCita int, in fechCita date, in horCita varchar(150), 
		in trat varchar (150), in descripCodAct varchar(150))
        Begin
			Update Citas c
				set c.fechaCita = fechCita,
                    c.horaCita = horCita,
                    c.tratamiento = trat,
                    c.desripCondActual = descripCodAct
					where codigoCita = codCita;
        End$$
Delimiter ;
-- Call sp_EditarCita()

-- -------- Login-------------------------
create table Rol(
	codigoRol int not null,
	tipoRol varchar(50) not null,
    Primary key PK_codigoRol(codigoRol)
)

Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    codigoRol int not null,
    primary key PK_codigoUsuario (codigoUsuario),
	constraint FK_Usuario_Rol foreign key (codigoRol)
	references Rol (codigoRol)
)

-- Procedimientos almacenados necesarios Rol
-- Agregar
Delimiter $$
	create procedure sp_AgregarRol(in codigoRol integer,in tipoRol varchar(50))
        Begin
        insert into Rol(codigoRol,tipoRol)
			values(codigoRol,tipoRol);
    end$$    
Delimiter ;
call sp_AgregarRol(1,'Estandar');
call sp_AgregarRol(2,'Administrador')

-- Procedimientos almacenados necesarios Usuario
-- Agregar
Delimiter $$
	create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100),
    in usuarioLogin varchar(50), in contrasena varchar(50), in codigoRol int)
        Begin
        insert into Usuario(nombreUsuario,apellidoUsuario,usuarioLogin,contrasena, codigoRol)
			values(nombreUsuario,apellidoUsuario,usuarioLogin,contrasena, codigoRol);
    end$$    
Delimiter ;
-- Listar
Delimiter $$
	create procedure sp_ListarUsuarios()
		Begin
			Select
            u.codigoUsuario,
            u.nombreUsuario,
            u.apellidoUsuario,
            u.usuarioLogin,
            u.contrasena,
            u.codigoRol
		from Usuario u;
        End$$
Delimiter ;
call sp_AgregarUsuario('Juan','Caceres','kinal','@123abc$',1);
call sp_AgregarUsuario('Juan','Caceres','root','12345',2);
call sp_ListarUsuarios();

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

Delimiter $$
	create procedure sp_ListarRoles()
		Begin
			Select
            r.codigoRol,
            r.tipoRol
		from Rol r;
        End$$
Delimiter ;
call sp_ListarRoles

Delimiter $$
	create procedure sp_BuscarRol(in codR int)
		Begin
			Select
            z.codigoRol,
            z.tipoRol
            from Rol z
				where codigoRol = codR;
        End$$
Delimiter ;