package com.valverde.scheduler.algorithm;

import com.valverde.scheduler.model.Classroom;
import com.valverde.scheduler.model.Professor;
import com.valverde.scheduler.model.ScheduleConfiguration;
import com.valverde.scheduler.model.StudentGroup;
import com.valverde.scheduler.util.ScheduleUtils;
import lombok.Getter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.valverde.scheduler.model.Class;

public class Schedule {

    public double calculateFitness() {
        int score = 0;
        final Set<Class> classes = classMap.keySet();
        for (final Class clazz : classes) {
            final int classStarted = classMap.get(clazz);
            final int room = classStarted % config.getRoomsAmount();
            final Classroom classroom = config.getClassrooms().get(room);
            if (!isAnotherClassOverlapping(clazz, classStarted)) {
                score++;
            } if (isEnoughSeatsInClassroom(classroom, clazz)) {
                score++;
            } if (isAllClassDurationInOneDay(clazz, classStarted)) {
                score++;
            }
            score += addFitnessIfStudentsAndProfessorHasNotAnotherClass(clazz, classStarted);
        }
        fitness = (double) score / classes.size();
        return fitness;
    }

    private boolean isAllClassDurationInOneDay(final Class clazz, final int classStarted) {
        int hourInDay = (classStarted / config.getRoomsAmount()) % config.getDayHours();
        return hourInDay + (clazz.getDuration() - 1) < config.getDayHours();
    }

    private int addFitnessIfStudentsAndProfessorHasNotAnotherClass(final Class clazz, final int classStarted) {
        final Professor professor = clazz.getProfessor();
        final StudentGroup studentGroup = clazz.getStudentGroup();
        boolean professorHasAnotherClass = false;
        boolean studentsHasAnotherClass = false;
        final int classEnd = getClassEndIndex(clazz, classStarted);
        /* for all class durations */
        for (int classIndex = classStarted; classIndex <= classEnd; classIndex += config.getRoomsAmount()) {
            int roomsAtTimeStart = (classIndex / config.getRoomsAmount())*config.getRoomsAmount();
            /* for every room in sameTime */
            for (int slotInSameTime = roomsAtTimeStart; slotInSameTime < roomsAtTimeStart + config.getRoomsAmount(); slotInSameTime++) {
                final List<Class> classesInSameTime = slots[slotInSameTime];
                for (Class classInSameTime : classesInSameTime) {
                    if (!classInSameTime.equals(clazz)) {
                        final Professor prof = classInSameTime.getProfessor();
                        final StudentGroup studGroup = classInSameTime.getStudentGroup();
                        if (prof.equals(professor)) {
                            professorHasAnotherClass = true;
                        } if (studGroup.equals(studentGroup)) {
                            studentsHasAnotherClass = true;
                        }
                    }
                }
            }
        }
        if (professorHasAnotherClass && studentsHasAnotherClass) {
            return 0;
        } else if (!professorHasAnotherClass && !studentsHasAnotherClass) {
            return 2;
        }
        return 1;
    }

    private boolean isEnoughSeatsInClassroom(final Classroom classroom, final Class clazz) {
        return classroom.getCapacity() >= clazz.getStudentGroup().getSize();
    }

    private boolean isAnotherClassOverlapping(final Class clazz, final int classStartTime) {
        final int classEndTime = getClassEndIndex(clazz, classStartTime);
        for (int time = classStartTime; time <= classEndTime; time += config.getRoomsAmount()) {
            if (isAnotherClassInSlot(slots[time], clazz))
                return true;
        }
        return false;
    }

    private boolean isAnotherClassInSlot(final List<Class> classes, final Class clazz) {
        for (Class cl : classes) {
            if (!cl.equals(clazz))
                return true;
        }
        return false;
    }

    private int getClassEndIndex(final Class clazz, final int classStart) {
        return classStart + ((clazz.getDuration() - 1) * config.getRoomsAmount());
    }

    public  Map<Class, Integer> getClassMap() {
        classMap = ScheduleUtils.prepareMap(slots);
        return classMap;
    }

    public Schedule(final ScheduleConfiguration config,
                    final List<Class>[] slots) {
        this.config = config;
        this.slots = slots;
        this.classMap = ScheduleUtils.prepareMap(slots);
    }

    @Getter
    private Double fitness;

    @Getter
    private ScheduleConfiguration config;

    @Getter
    private List<Class>[] slots;

    private Map<Class, Integer> classMap;
}