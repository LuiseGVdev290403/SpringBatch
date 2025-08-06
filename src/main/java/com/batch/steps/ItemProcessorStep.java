package com.batch.steps;

import com.batch.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
public class ItemProcessorStep implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("-----------> Inicio del paso de Procesamiento <-----------");
        List<Person> personList = (List<Person>) chunkContext.getStepContext()
                                                    .getStepExecution()
                                                    .getJobExecution()
                                                    .getExecutionContext()
                                                    .get("personList");

        List<Person> finalList = personList.stream().map(person -> {
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
            person.setInsertionDate(dateTimeFormat.format(LocalDateTime.now()));
            return person;
        }).toList();

        chunkContext.getStepContext()
                        .getStepExecution()
                        .getJobExecution()
                        .getExecutionContext()
                        .put("personList", finalList);
        log.info("-----------> Fin del paso de Procesamiento <-----------");
        return RepeatStatus.FINISHED;
    }
}
