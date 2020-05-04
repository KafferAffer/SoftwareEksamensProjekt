package Applikationslag.Infrastruktur.ServiceImplementationer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;

import Applikationslag.Data.Datavedholdelsesklasser.AktivitetData;
import Applikationslag.Data.Datavedholdelsesklasser.MedarbejderData;
import Applikationslag.Domaeneklasser.Aktivitet;
import Applikationslag.Domaeneklasser.Medarbejder;
import Applikationslag.Domaeneklasser.Projekt;
import Applikationslag.Infrastruktur.ServiceInterfaces.IMedarbejderManager;
import Applikationslag.Redskaber.Dates;
import Applikationslag.Redskaber.GlobaleVariable;

public class MedarbejderManager implements IMedarbejderManager {

	@Override
	public Boolean GemMedarbejder(Medarbejder medarbejder) {
		if(!MedarbejderData.Bibliotek.entrySet().stream()
				.anyMatch(e -> e.getValue().getNavn() == medarbejder.getNavn()))
			return (MedarbejderData.Bibliotek.put(medarbejder.ID(), medarbejder) == null);
		else
			return false;
	}

	@Override
	public long AktiviteterIDenneUge(int week, int year, Medarbejder medarbejder) {
		return AktivitetData.Bibliotek.entrySet().stream()
			.filter(e -> e.getValue().Medarbejder() != null)
			.filter(e -> e.getValue().Medarbejder().ID() == medarbejder.ID())
			.filter(e -> 
					((
							((e.getValue().getStartaar() < year) 
								|| 
								(e.getValue().getStartaar() == year 
									&&
								(e.getValue().getStartUge() <= week)))
						&&
							((e.getValue().getSlutaar() > year) 
								|| 
								(e.getValue().getSlutaar() == year 
									&& 
								(e.getValue().getSlutUge() >= week)))	
					))
					).count();
	}
	
	@Override
	public List<Entry<UUID, Medarbejder>> hentAlleMedarbejdere() {
		return MedarbejderData.Bibliotek.entrySet().stream().collect(Collectors.toList());
	}
	
	public HashMap<UUID,Medarbejder> AlleLedigeMedarbejdere(int weekStart, int weekSlut, int yearStart, int yearSlut) {
		HashMap<UUID, Medarbejder> result = (HashMap<UUID, Medarbejder>) MedarbejderData.Bibliotek.clone();
		
		for(int i = yearStart; i <= yearSlut; i ++)
		{
			for(int j = (i == yearStart ? weekStart : 0); j < (i == yearSlut ? weekSlut : 0); j++)
			{
				for(Entry<UUID, Medarbejder> m : result.entrySet())
				{
					if(AktiviteterIDenneUge(j, i, m.getValue()) > GlobaleVariable.MaksimaleVagter())
					{
						result.remove(m.getValue().ID());
					}
				}
			}
		}
		return result;
	}

	@Override
	public Boolean MedarbejderLedig(int weekStart, int weekSlut, int yearStart, int yearSlut, Medarbejder medarbejder) {
		for(int i = yearStart; i <= yearSlut; i ++)
		{
			for(int j = (i == yearStart ? weekStart : 0); j < (i == yearSlut ? weekSlut : 0); j++)
			{
				if(AktiviteterIDenneUge(j, i, medarbejder) > GlobaleVariable.MaksimaleVagter())
				{
					return false;
				}
			}
		}
		return true;
	}

}
