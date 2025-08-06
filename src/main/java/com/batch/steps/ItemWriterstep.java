package com.batch.steps;

import com.batch.entities.Person;
import com.batch.service.IPersonServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ItemWriterstep implements Tasklet {

    @Autowired
    private IPersonServices iPersonServices;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("-----------> Inicio del paso de Escritura <-----------");
        List<Person> personList = (List<Person>) chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("personList");

        // imprime personas
        personList.forEach( person -> {
            if (person != null) {
                log.info(person.toString());
            }
        });
        //guarda en la bd
        iPersonServices.saveAll(personList);

        log.info("-----------> Final del paso de Escritura <-----------");
        return RepeatStatus.FINISHED;
    }
}
