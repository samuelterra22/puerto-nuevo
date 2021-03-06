package br.edu.ifmg.samuelterra.model.events.railwaycompositionEvents;

import br.edu.ifmg.samuelterra.ContainerShipFactory;
import br.edu.ifmg.samuelterra.model.entities.Equip;
import br.edu.ifmg.samuelterra.model.entities.Quay;
import br.edu.ifmg.samuelterra.model.entities.ships.Ship;
import br.edu.ifmg.samuelterra.model.events.Event;
import br.edu.ifmg.samuelterra.model.events.equipEvents.EndUndockingEvent;
import br.edu.ifmg.samuelterra.model.random.Random;
import br.edu.ifmg.samuelterra.model.random.RandomConstants;
import br.edu.ifmg.samuelterra.model.system.Systema;

/**
 TrainArrivingEvent

 */

public class EndDockingEvent extends Event{
    public EndDockingEvent(){

    }

    public void execute(Equip equip, Systema system){
        //atualiza o tempo do sistema
        system.setClock(this.getOccurrenceTime());


        //se navio para desatracar
        if (system.getEntityQueueSet().getEntityQueue("ship waiting undock").available()){
            //retira navio
            Ship ship  = (Ship) system.getEntityQueueSet().getEntity("ship waiting undock");

            Event event = new EndUndockingEvent();
            event.setOccurrenceTime(this.getOccurrenceTime()+system.getRandomTimeGenerator().getTime("undocking"));
            system.getFutureEventList().addEvent(event);
        }

        //se navio para atracar e cais disponível
        else if(system.getEntityQueueSet().getEntityQueue("ship waiting dock").available()&&system.getEntityQueueSet().getEntityQueue("quay").available()){
            //retira navio e reserva cais
            Ship ship  = (Ship) system.getEntityQueueSet().getEntity("ship waiting dock");
            Quay quay = (Quay) system.getEntityQueueSet().getEntity("quay");

            /* agendando na FEL um evento de fim de atracamento

               cria o novo evento
               setando seus paramêtros
               e estabelece o tempo de ocorrencia
               com base no tempo atual
               + o sorteio da duração do atracamento
            */


            Event event = new EndDockingEvent();
            event.setOccurrenceTime(this.getOccurrenceTime()+system.getRandomTimeGenerator().getTime("docking"));
            system.getFutureEventList().addEvent(event);
        }
        else{
            //entra em fila de espera
            system.getEntityQueueSet().addEntity("equip", equip);
        }

    }
}
