package com.tngtech.java.junit.dataprovider.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.tngtech.java.junit.dataprovider.BaseTest;
import com.tngtech.java.junit.dataprovider.DataProvider;

@RunWith(MockitoJUnitRunner.class)
public class DataConverterTest extends BaseTest {

    @InjectMocks
    private DataConverter underTest;

    @Mock
    private DataProvider dataProvider;

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsNull() {
        // Given:

        // When:
        boolean result = underTest.canConvert(null);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsObject() {
        // Given:

        // When:
        boolean result = underTest.canConvert(Object.class);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsString() {
        // Given:

        // When:
        boolean result = underTest.canConvert(String.class);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsList() {
        // Given:
        Type type = List.class;

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsListOfObject() {
        // Given:
        Type type = getMethod("methodReturningListOfObject").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsListOfIterable() {
        // Given:
        Type type = getMethod("methodReturningListOfIterableOfObject").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsIterableOfIterable() {
        // Given:
        Type type = getMethod("methodReturningIterableOfIterableOfObject").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsSetOfSet() {
        // Given:
        Type type = getMethod("methodReturningSetOfSetOfObject").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnFalseIfTypeIsTwoArgList() {
        // Given:
        Type type = getMethod("methodReturningTwoArgListOfListsOfObject").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isFalse();
    }

    @Test
    public void testCanConvertShouldReturnTrueIfTypeIsObjectArrayArray() {
        // Given:
        Type type = getMethod("methodReturningObjectArrayArray").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isTrue();
    }

    @Test
    public void testCanConvertShouldReturnTrueIfTypeIsListListObject() {
        // Given:
        Type type = getMethod("methodReturningListOfListOfObject").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isTrue();
    }

    @Test
    public void testCanConvertShouldReturnTrueIfTypeIsSubListSubListObject() {
        // Given:
        Type type = getMethod("methodReturningSubListOfSubListOfObject").getGenericReturnType();

        // When:
        boolean result = underTest.canConvert(type);

        // Then:
        assertThat(result).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void testConvertShouldThrowNullPointerExceptionIfParameterTypesIsNull() {
        // Given:
        Object data = null;
        Class<?>[] parameterTypes = null;

        // When:
        underTest.convert(data, parameterTypes, dataProvider);

        // Then: expect exception
    }

    @Test(expected = NullPointerException.class)
    public void testConvertShouldThrowNullPointerExceptionIfDataProviderIsNull() {
        // Given:
        Object data = null;
        Class<?>[] parameterTypes = new Class<?>[] { Object.class };

        // When:
        underTest.convert(data, parameterTypes, null);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertShouldThrowIllegalArgumentExceptionIfParameterTypesIsEmpty() {
        // Given:
        Object data = null;
        Class<?>[] parameterTypes = new Class<?>[0];

        // When:
        underTest.convert(data, parameterTypes, dataProvider);

        // Then: expect exception
    }

    @Test(expected = ClassCastException.class)
    public void testConvertShouldThrowClassCastExceptionIfDataIsNull() {
        // Given:
        Object data = null;
        Class<?>[] parameterTypes = new Class<?>[] { String.class };

        // When:
        underTest.convert(data, parameterTypes, dataProvider);

        // Then: expect exception
    }

    @Test(expected = ClassCastException.class)
    public void testConvertShouldThrowClassCastExceptionIfDataIsNotConvertable() {
        // Given:
        Object data = "not convertable";
        Class<?>[] parameterTypes = new Class<?>[] { Integer.class };

        // When:
        underTest.convert(data, parameterTypes, dataProvider);

        // Then: expect exception
    }

    @Test
    public void testConvertShouldReturnOneElementForObjectArrayArrayWithOneElement() {
        // Given:
        Object[][] data = new Object[][] { { 1 } };
        Class<?>[] parameterTypes = new Class<?>[] { int.class };

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(data[0]);
    }

    @Test
    public void testConvertShouldReturnMultipleElementsForObjectArrayArrayWithMultipleElements() {
        // Given:
        Object[][] data = new Object[][] { { "11", 22L, 3.3 }, { "44", 55L, 6.6 }, { "77", 88L, 9.9 } };
        Class<?>[] parameterTypes = new Class<?>[] { String.class, long.class, double.class };

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo(data[0]);
        assertThat(result.get(1)).isEqualTo(data[1]);
        assertThat(result.get(2)).isEqualTo(data[2]);
    }

    @Test
    public void testConvertShouldReturnOneElementForListOfListOfObjectWithOneElement() {
        // Given:
        @SuppressWarnings("unchecked")
        List<List<Character>> data = list(list('a'));
        Class<?>[] parameterTypes = new Class<?>[] { char.class };

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(data.get(0).toArray());
    }

    @Test
    public void testConvertShouldReturnMultipleElementsForListOfListOfObjectWithMultipleElements() {
        // Given:
        @SuppressWarnings("unchecked")
        List<List<?>> data = list(this.<Object> list('x', "foo"), list('y', "bar"), list('z', "baz"));
        Class<?>[] parameterTypes = new Class<?>[] { char.class, String.class };

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo(data.get(0).toArray());
        assertThat(result.get(1)).isEqualTo(data.get(1).toArray());
        assertThat(result.get(2)).isEqualTo(data.get(2).toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertShouldThrowIllegalArgumentExceptionIfLengthOfSplitDataAndTargetTypesDiffer() {
        // Given:
        String[] data = new String[] { "1,2" };
        Class<?>[] parameterTypes = new Class[] { int.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        underTest.convert(data, parameterTypes, dataProvider);

        // Then: expect exception
    }

    @Test
    public void testConvertShouldReturnOneElementForStringArrayWithOneElementSplitByComma() {
        // Given:
        String[] data = new String[] { "foo,true" };
        Class<?>[] parameterTypes = new Class<?>[] { String.class, boolean.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new Object[] { "foo", true });
    }

    @Test
    public void testConvertShouldReturnOneElementForStringArrayWithOneElementSplitByPipe() {
        // Given:
        String[] data = new String[] { "bar|false" };
        Class<?>[] parameterTypes = new Class<?>[] { String.class, boolean.class };

        doReturn("\\|").when(dataProvider).splitBy();

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new Object[] { "bar", false });
    }

    @Test
    public void testConvertShouldReturnOneElementForStringArrayWithOneElementSplitByMultipleWhitespaces() {
        // Given:
        String[] data = new String[] { "baz    2" };
        Class<?>[] parameterTypes = new Class<?>[] { String.class, int.class };

        doReturn("\\s+").when(dataProvider).splitBy();

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(new Object[] { "baz", 2 });
    }

    @Test
    public void testConvertShouldReturnMultipleElementsForStringArrayWithMultipleElements() {
        // Given:
        String[] data = new String[] { "1, 2, 3, 4.0, e", "6, 7, 8, 9.0, i" };
        Class<?>[] parameterTypes = new Class<?>[] { byte.class, int.class, long.class, double.class, char.class };

        doReturn(",").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).trimValues();

        // When:
        List<Object[]> result = underTest.convert(data, parameterTypes, dataProvider);

        // Then:
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(new Object[] { (byte) 1, 2, 3l, 4.0, 'e' });
        assertThat(result.get(1)).isEqualTo(new Object[] { (byte) 6, 7, 8l, 9.0, 'i' });
    }

    @Test(expected = NullPointerException.class)
    public void testCheckTestMethodArgumentsShouldThrowNullPointerExceptionIfArgumentsIsNull() {
        // Given:

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(null, new Class<?>[0]);

        // Then: expect exception
    }

    @Test(expected = NullPointerException.class)
    public void testCheckTestMethodArgumentsShouldThrowNullPointerExceptionIfParameterTypesIsNull() {
        // Given:

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(new ArrayList<Object[]>(), null);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTestMethodArgumentsShouldThrowIllegalArgumentExceptionIfLengthOfArgumentsAndParameterTypesDoesNotMatch() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[0], new Object[0]);
        Class<?>[] parameterTypes = new Class<?>[] { int.class, String.class, boolean.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTestMethodArgumentsShouldThrowIllegalArgumentExceptionIfSingleArgumentIsNotAssignableToParameterTypeOnOnlyTest() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { "1" });
        Class<?>[] parameterTypes = new Class<?>[] { int.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTestMethodArgumentsShouldThrowIllegalArgumentExceptionIfSingleArgumentIsNotAssignableToParameterTypeOnSecondTest() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { 1 }, new Object[] { "2" }, new Object[] { 3 });
        Class<?>[] parameterTypes = new Class<?>[] { int.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTestMethodArgumentsShouldThrowIllegalArgumentExceptionIfAnyArgumentTypeIsNotAssignableToParameterTypeOnOnlyTest() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { 1, "1", 7l });
        Class<?>[] parameterTypes = new Class<?>[] { int.class, String.class, boolean.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTestMethodArgumentsShouldThrowIllegalArgumentExceptionIfAnyArgumentTypeIsNotAssignableToParameterTypeOnSecondTest() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { 1, "1", true }, new Object[] { 2, "2", 2l });
        Class<?>[] parameterTypes = new Class<?>[] { int.class, String.class, boolean.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then: expect exception
    }

    @Test
    public void testCheckTestMethodArgumentsShouldNotThrowErrorIfArgumentTypesEqualsParameterTypesOnOnlyTest() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { Character.valueOf('a') });
        Class<?>[] parameterTypes = new Class<?>[] { Character.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldNotThrowErrorIfArgumentTypesEqualParameterTypesExactlyOnMultipleTests() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { "a", Boolean.TRUE }, new Object[] { "b", Boolean.FALSE });
        Class<?>[] parameterTypes = new Class<?>[] { String.class, Boolean.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldNotThrowErrorIfArgumentsAreAssignableToParameterTypes() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { Long.valueOf(1l) }, new Object[] { Integer.valueOf(2) });
        Class<?>[] parameterTypes = new Class<?>[] { Number.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldNotThrowErrorIfArgumentsAreWrappedLongPrimitivesOfParameterTypes() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { Long.valueOf(1l) }, new Object[] { Long.valueOf(2l) });
        Class<?>[] parameterTypes = new Class<?>[] { long.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldNotThrowErrorIfArgumentsAreWrappedMultiplePrimitivesOfParameterTypes() {
        // Given:
        // @formatter:off
        List<Object[]> arguments = listOfArrays(
                new Object[] { Byte.valueOf((byte) 1), Character.valueOf('a'), Double.valueOf(1.1) },
                new Object[] { Byte.valueOf((byte) 2), Character.valueOf('b'), Double.valueOf(2.2) },
                new Object[] { Byte.valueOf((byte) 3), Character.valueOf('c'), Double.valueOf(3.3) }
            );
        // @formatter:on
        Class<?>[] parameterTypes = new Class<?>[] { byte.class, char.class, double.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldBeAwareOfWideningPrimitiveConversionsOfByte() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5 });

        Class<?>[] parameterTypes = new Class<?>[] { short.class, int.class, long.class, float.class, double.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldBeAwareOfWideningPrimitiveConversionsOfShort() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { (short) 1, (short) 2, (short) 3, (short) 4 });

        Class<?>[] parameterTypes = new Class<?>[] { int.class, long.class, float.class, double.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldBeAwareOfWideningPrimitiveConversionsOfChar() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { (char) 1, (char) 2, (char) 3, (char) 4 });

        Class<?>[] parameterTypes = new Class<?>[] { int.class, long.class, float.class, double.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldBeAwareOfWideningPrimitiveConversionsOfInt() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { (int) 1, (int) 2, (int) 3 });

        /* int to long, float, or double */
        Class<?>[] parameterTypes = new Class<?>[] { long.class, float.class, double.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test
    public void testCheckTestMethodArgumentsShouldBeAwareOfWideningPrimitiveConversionsOfFloat() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { (float) 1 });

        Class<?>[] parameterTypes = new Class<?>[] { float.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then:
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckTestMethodArgumentsShouldThrowExceptionForNonWideningConversionsOfLong() {
        // Given:
        List<Object[]> arguments = listOfArrays(new Object[] { (long) 1 });

        Class<?>[] parameterTypes = new Class<?>[] { int.class };

        // When:
        underTest.checkIfArgumentsMatchParameterTypes(arguments, parameterTypes);

        // Then: expect exception
    }

    @Test
    public void testGetParametersForShouldCorrectlyParseAllPrimitiveTypes() {
        // Given:
        String data = "true,1,c,2,3,4,5.5,6.6";
        Class<?>[] parameterTypes = new Class[] { boolean.class, byte.class, char.class, short.class, int.class,
                long.class, float.class, double.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 10);

        // Then:
        assertThat(result).isEqualTo(new Object[] { true, (byte) 1, 'c', (short) 2, 3, 4L, 5.5f, 6.6d });
    }

    @Test
    public void testGetParametersForShouldCorrectlyParseAllPrimitiveTypesAsJavaString() {
        // Given:
        String data = "-5;2014l;-1.234567f;-901e-3";
        Class<?>[] parameterTypes = new Class[] { int.class, long.class, float.class, double.class };

        doReturn(";").when(dataProvider).splitBy();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 11);

        // Then:
        assertThat(result).isEqualTo(new Object[] { -5, 2014l, -1.234567f, -0.901d });
    }

    @Test
    public void testGetParametersForShouldNotTrimValuesIfSettingsTrimIsFalse() {
        // Given:
        String data = " foo|  bar   |baz    ";
        Class<?>[] parameterTypes = new Class[] { String.class, String.class, String.class };

        doReturn("\\|").when(dataProvider).splitBy();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 12);

        // Then:
        assertThat(result).isEqualTo(new Object[] { " foo", "  bar   ", "baz    " });
    }

    @Test
    public void testGetParametersForShouldTrimAndParseAllPrimitiveTypesIfSettingsTrimIsTrue() {
        // Given:
        String data = "   false   ;    11    ;    z    ;  22       ;   33   ;44      ;   55.55     ;  66.66     ";
        Class<?>[] parameterTypes = new Class[] { boolean.class, byte.class, char.class, short.class, int.class,
                long.class, float.class, double.class };

        doReturn(";").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).trimValues();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 13);

        // Then:
        assertThat(result).isEqualTo(new Object[] { false, (byte) 11, 'z', (short) 22, 33, 44L, 55.55f, 66.66d });
    }

    @Test
    public void testGetParametersForShouldTrimNonSpaceWhitespaceCharsIfSettingsTrimIsTrue() {
        // Given:
        String data = "\n-1f\n,\r-2\r,\t3.0d\t";
        Class<?>[] parameterTypes = new Class[] { float.class, int.class, double.class };

        doReturn(",").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).trimValues();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 20);

        // Then:
        assertThat(result).isEqualTo(new Object[] { -1f, -2, 3d });
    }

    @Test
    public void testGetParametersForShouldNotTrimNonBreakingSpaceEvenIfSettingsTrimIsTrue() {
        // Given:
        String data = "\u00A0test\u00A0";
        Class<?>[] parameterTypes = new Class[] { String.class };

        doReturn(",").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).trimValues();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 21);

        // Then:
        assertThat(result).isEqualTo(new Object[] { "\u00A0test\u00A0" });
    }

    @Test
    public void testGetParametersForShouldCorrectlyHandleLeadingEmptyString() {
        // Given:
        String data = "/true";
        Class<?>[] parameterTypes = new Class[] { String.class, boolean.class };

        doReturn("/").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).trimValues();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 30);

        // Then:
        assertThat(result).isEqualTo(new Object[] { "", true });
    }

    @Test
    public void testGetParametersForShouldCorrectlyHandleTrailingEmptyString() {
        // Given:
        String data = "1 ";
        Class<?>[] parameterTypes = new Class[] { int.class, String.class };

        doReturn(" ").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).trimValues();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 31);

        // Then:
        assertThat(result).isEqualTo(new Object[] { 1, "" });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParametersForShouldThrowIllegalArgumentExceptionIfCharHasNotLengthOne() {
        // Given:
        String data = "noChar";
        Class<?>[] parameterTypes = new Class[] { char.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        underTest.getParametersFor(data, parameterTypes, dataProvider, 40);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParametersForShouldThrowIllegalArgumentExceptionIfValueOfThrowsNumberFormatException() {
        // Given:
        String data = "noInt";
        Class<?>[] parameterTypes = new Class[] { int.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        underTest.getParametersFor(data, parameterTypes, dataProvider, 41);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParametersForShouldThrowIllegalArgumentExceptionForTargetTypeConstructorWithStringArgWhichThrowsException() {
        // Given:
        String data = "noInt";
        Class<?>[] parameterTypes = new Class[] { BigInteger.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        underTest.getParametersFor(data, parameterTypes, dataProvider, 42);

        // Then: expect exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParametersForShouldThrowIllegalArgumentExceptionForUnsupportedTargetType() {
        // Given:
        String data = "noObject";
        Class<?>[] parameterTypes = new Class[] { Object.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        underTest.getParametersFor(data, parameterTypes, dataProvider, 43);

        // Then: expect exception
    }

    @Test
    public void testGetParametersForShouldCorrectlyParseEnum() {
        // Given:
        String data = " VAL1,  VAL2 ";
        Class<?>[] parameterTypes = new Class[] { TestEnum.class, TestEnum.class };

        doReturn(",").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).trimValues();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 50);

        // Then:
        assertThat(result).isEqualTo(new Object[] { TestEnum.VAL1, TestEnum.VAL2 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParametersForShouldThrowIllegalArgumentExceptionIfEnumValueIsInvalid() {
        // Given:
        String data = "UNKNOW_ENUM_VALUE";
        Class<?>[] parameterTypes = new Class[] { TestEnum.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        underTest.getParametersFor(data, parameterTypes, dataProvider, 51);

        // Then: expect exception
    }

    @Test
    public void testGetParametersForShouldCorrectlyParseAllPrimitiveWrapperTypes() {
        // Given:
        String data = "true,1,c,2,3,4,5.5,6.6";
        Class<?>[] parameterTypes = new Class[] { Boolean.class, Byte.class, Character.class, Short.class,
                Integer.class, Long.class, Float.class, Double.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 60);

        // Then:
        assertThat(result).isEqualTo(
                new Object[] { Boolean.TRUE, Byte.valueOf((byte) 1), Character.valueOf('c'), Short.valueOf((short) 2),
                        Integer.valueOf(3), Long.valueOf(4L), Float.valueOf(5.5f), Double.valueOf(6.6d) });
    }

    @Test
    public void testGetParametersForShouldParseNullValuesAsStringIfSettingsConvertNullsIsFalse() {
        // Given:
        String data = "null#null";
        Class<?>[] parameterTypes = new Class[] { String.class, String.class };

        doReturn("#").when(dataProvider).splitBy();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 70);

        // Then:
        assertThat(result).isEqualTo(new Object[] { "null", "null" });
    }

    @Test
    public void testGetParametersForShouldParseNullValuesAsNullObjectIfSettingsConvertNullsIsTrue() {
        // Given:
        String data = "null,null";
        Class<?>[] parameterTypes = new Class[] { String.class, String.class };

        doReturn(",").when(dataProvider).splitBy();
        doReturn(true).when(dataProvider).convertNulls();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 71);

        // Then:
        assertThat(result).isEqualTo(new Object[] { null, null });
    }

    @Test
    public void testGetParametersForShouldReturnNullObjectWrappedInObjectArray() {
        // Given:
        String data = null;
        Class<?>[] parameterTypes = new Class<?>[] { Integer.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 72);

        // Then:
        assertThat(result).isEqualTo(new Object[] { null });
    }

    @Test
    public void testGetParametersForShouldCorrectlyUseConstructorWithSingleStringArg() {
        // Given:
        String data = "home/schmida";
        Class<?>[] parameterTypes = new Class<?>[] { File.class };

        doReturn(",").when(dataProvider).splitBy();

        // When:
        Object[] result = underTest.getParametersFor(data, parameterTypes, dataProvider, 80);

        // Then:
        assertThat(result).isEqualTo(new Object[] { new File("home/schmida") });
    }

    // -- methods used as Method objects -------------------------------------------------------------------------------

    public static List<Object> methodReturningListOfObject() {
        return null;
    }

    public static List<Iterable<Object>> methodReturningListOfIterableOfObject() {
        return null;
    }

    public static Iterable<Iterable<Object>> methodReturningIterableOfIterableOfObject() {
        return null;
    }

    public static Set<Set<Object>> methodReturningSetOfSetOfObject() {
        return null;
    }

    @SuppressWarnings("serial")
    private static class TwoArgList<A, B> extends ArrayList<A> {
        // not required for now :-)
    }

    public static TwoArgList<List<Object>, List<Object>> methodReturningTwoArgListOfListsOfObject() {
        return null;
    }

    public static Object[][] methodReturningObjectArrayArray() {
        return null;
    }

    public static List<List<Object>> methodReturningListOfListOfObject() {
        return null;
    }

    @SuppressWarnings("serial")
    private static class SubList<A> extends ArrayList<A> {
        // not required for now :-)
    }

    public static SubList<SubList<Object>> methodReturningSubListOfSubListOfObject() {
        return null;
    }
}
