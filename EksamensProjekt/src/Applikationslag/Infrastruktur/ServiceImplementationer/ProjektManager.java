package Applikationslag.Infrastruktur.ServiceImplementationer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import Applikationslag.Data.Datavedholdelsesklasser.*;
import Applikationslag.Domaeneklasser.Aktivitet;
//import Applikationslag.Domaeneklasser.Assistance;
import Applikationslag.Domaeneklasser.Medarbejder;
import Applikationslag.Domaeneklasser.Projekt;
import Applikationslag.Infrastruktur.ServiceInterfaces.*;

public class ProjektManager implements IProjektManager{

	//@Override
	public Boolean GemProjekt(Projekt projekt) {
		if (!ProjektData.Bibliotek.entrySet().stream()
				.anyMatch(e -> e.getValue().getNavn().equals(projekt.getNavn())) )
			return (ProjektData.Bibliotek.put(projekt.ID(), projekt) == null);
		else 
			return false;
	}
	
	public Projekt projektUdFraNavn(String navn) {
		for(Entry<UUID, Projekt> e : ProjektData.Bibliotek.entrySet()) {
			if (e.getValue().getNavn().equals(navn)) {
				return e.getValue();
			}
		}
		return null;
	}

	public Boolean eksisterer(Projekt projekt)
	{
		return ProjektData.Bibliotek.containsValue(projekt);
	}

	@Override
	public Boolean fjern(Projekt projekt) {
		if (AktivitetData.Bibliotek.entrySet().stream()
				.anyMatch(e -> e.getValue().getProjekt() ==  projekt))
			return false;
	else
		ProjektData.Bibliotek.remove(projekt.ID());
	return true;
	}
	
	public List<Entry<UUID, Projekt>> hentAlleProjekter() {
		return ((HashMap<UUID, Projekt>)ProjektData.Bibliotek.clone())
				.entrySet().stream().collect(Collectors.toList());
	}


}
