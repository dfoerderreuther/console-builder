package de.eleon.console.builder;

import com.google.common.base.Optional;
import de.eleon.console.builder.functional.Transformer;
import de.eleon.console.builder.functional.Transformers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static de.eleon.test.matchers.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnswerOptionalTest {

    enum TestEnum {a, b, c}

    @Mock
    AskBuilder askBuilder;

    AnswerOptional underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new AnswerOptional(askBuilder);
    }

    @Test
    public void shouldAnswerOptionalString() {
        when(askBuilder.answer()).thenReturn("test");
        Optional<String> answer = underTest.answer();
        assertThat(answer, isPresent());
        assertThat(answer.get(), is("test"));
    }

    @Test
    public void shouldAnswerAbsent() {
        when(askBuilder.answer()).thenReturn("");
        Optional<String> answer = underTest.answer();
        assertThat(answer, not(isPresent()));
    }

    @Test
    public void shouldAnswerTransformedOptionalWithMessage() {
        Transformer<Integer> transformer = Transformers.toInteger();
        when(askBuilder.answer(transformer, "error message")).thenReturn(17);
        Optional<Integer> answer = underTest.answer(transformer, "error message");
        assertThat(answer, isPresent());
        assertThat(answer.get(), is(17));
    }

    @Test
    public void shouldAnswerTransformedAbsentWithMessage() {
        Transformer<Integer> transformer = Transformers.toInteger();
        when(askBuilder.answer(transformer, "error message")).thenReturn(null);
        Optional<Integer> answer = underTest.answer(transformer, "error message");
        assertThat(answer, not(isPresent()));
    }

    @Test
    public void shouldAnswerTransformedOptional() {
        Transformer<Integer> transformer = Transformers.toInteger();
        when(askBuilder.answer(transformer)).thenReturn(17);
        Optional<Integer> answer = underTest.answer(transformer);
        assertThat(answer, isPresent());
        assertThat(answer.get(), is(17));
    }

    @Test
    public void shouldAnswerTransformedAbsent() {
        Transformer<Integer> transformer = Transformers.toInteger();
        when(askBuilder.answer(transformer)).thenReturn(null);
        Optional<Integer> answer = underTest.answer(transformer);
        assertThat(answer, not(isPresent()));
    }

    @Test
    public void shouldAnswerEnumOptional() {
        when(askBuilder.answer(TestEnum.class)).thenReturn(TestEnum.a);
        Optional<TestEnum> answer = underTest.answer(TestEnum.class);
        assertThat(answer, isPresent());
        assertThat(answer.get(), is(TestEnum.a));
    }

    @Test
    public void shouldAnswerEnumOptionalAbsent() {
        when(askBuilder.answer(TestEnum.class)).thenReturn(null);
        Optional<TestEnum> answer = underTest.answer(TestEnum.class);
        assertThat(answer, not(isPresent()));
    }

    @Test
    public void shouldAnswerEnumOptional_withMessage() {
        when(askBuilder.answer(TestEnum.class, "error message")).thenReturn(TestEnum.a);
        Optional<TestEnum> answer = underTest.answer(TestEnum.class, "error message");
        assertThat(answer, isPresent());
        assertThat(answer.get(), is(TestEnum.a));
    }

    @Test
    public void shouldAnswerEnumOptionalAbsent_withMessage() {
        when(askBuilder.answer(TestEnum.class, "error message")).thenReturn(null);
        Optional<TestEnum> answer = underTest.answer(TestEnum.class, "error message");
        assertThat(answer, not(isPresent()));
    }


}