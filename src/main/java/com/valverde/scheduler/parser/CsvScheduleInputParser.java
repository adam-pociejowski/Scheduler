package com.valverde.scheduler.parser;

import com.valverde.scheduler.model.*;
import com.valverde.scheduler.model.Class;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.valverde.scheduler.enums.InputType.*;

public class CsvScheduleInputParser {

    public ScheduleInput parse(final String path, final String separator) throws IOException {
        final File file = new File(path);
        final List<String> lines = FileUtils.readLines(file, "UTF-8");

        for (String line : lines) {
            if (!line.startsWith(COMMENTS_TAG)) {
                final String[] values = line.split(separator);
                final String label = values[0];
                if (label.equals(CLASS.getLabel())) {
                    parseClass(values);
                } else if (label.equals(PROFESSOR.getLabel())) {
                    parseProfessor(values);
                } else if (label.equals(STUDENT_GROUP.getLabel())) {
                    parseStudentGroup(values);
                } else if (label.equals(CLASSROOM.getLabel())) {
                    parseClassroom(values);
                } else if (label.equals(COURSE.getLabel())) {
                    parseCourse(values);
                }
            }
        }
        return new ScheduleInput(classes, classrooms);
    }

    private void parseClass(final String[] values) {
        final Class clazz = new Class();
        clazz.setId(getLong(values[1]));
        clazz.setProfessor(findProfessor(getLong(values[2])));
        clazz.setCourse(findCourse(getLong(values[3])));
        clazz.setStudentGroup(findStudentGroup(getLong(values[4])));
        clazz.setDuration(getInt(values[5]));
        classes.add(clazz);
    }

    private StudentGroup findStudentGroup(final Long id) {
        for (StudentGroup studentGroup : studentGroups) {
            if (studentGroup.getId().equals(id))
                return studentGroup;
        }
        throw new RuntimeException("No studentGroup found with id: "+id);
    }

    private Professor findProfessor(final Long id) {
        for (Professor professor : professors) {
            if (professor.getId().equals(id))
                return professor;
        }
        throw new RuntimeException("No professor found with id: "+id);
    }

    private Course findCourse(final Long id) {
        for (Course course : courses) {
            if (course.getId().equals(id))
                return course;
        }
        throw new RuntimeException("No course found with id: "+id);
    }

    private void parseCourse(final String[] values) {
        final Course course = new Course();
        course.setId(getLong(values[1]));
        course.setName(values[2]);
        courses.add(course);
    }

    private void parseClassroom(final String[] values) {
        final Classroom classroom = new Classroom();
        classroom.setId(getLong(values[1]));
        classroom.setName(values[2]);
        classroom.setCapacity(getInt(values[3]));
        classrooms.add(classroom);
    }

    private void parseStudentGroup(final String[] values) {
        final StudentGroup studentGroup = new StudentGroup();
        studentGroup.setId(getLong(values[1]));
        studentGroup.setName(values[2]);
        studentGroup.setSize(getInt(values[3]));
        studentGroups.add(studentGroup);
    }

    private void parseProfessor(final String[] values) {
        final Professor professor = new Professor();
        professor.setId(getLong(values[1]));
        professor.setName(values[2]);
        professors.add(professor);
    }

    private Long getLong(final String value) {
        return Long.parseLong(value);
    }

    private Integer getInt(final String value) {
        return Integer.parseInt(value);
    }

    private List<Professor> professors = new ArrayList<>();

    private List<StudentGroup> studentGroups = new ArrayList<>();

    private List<Classroom> classrooms = new ArrayList<>();

    private List<Course> courses = new ArrayList<>();

    private List<Class> classes = new ArrayList<>();

    private static final String COMMENTS_TAG = "--";
}
