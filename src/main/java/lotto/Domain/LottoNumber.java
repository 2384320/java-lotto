package lotto.Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LottoNumber {
    private static final int MINIMUM_NUMBER_RANGE = 1;
    private static final int MAXIMUM_NUMBER_RANGE = 45;

    private static final Map<Integer, LottoNumber> lottoNumberVOCache = new HashMap<>();

    private final int lottoNumber;

    public LottoNumber(int lottoNumber) {
        validateRange(lottoNumber);
        this.lottoNumber = lottoNumber;
    }

    private void validateRange(int lottoNumber) {
        if (lottoNumber < MINIMUM_NUMBER_RANGE
                || lottoNumber > MAXIMUM_NUMBER_RANGE)
            throw new IllegalArgumentException("[ERROR] 번호의 범위는 1부터 45까지여야 합니다.");
    }

    public int mapToInt() {
        return lottoNumber;
    }

    public static LottoNumber from(int lottoNumber) {
        if (lottoNumberVOCache.containsKey(lottoNumber))
            return lottoNumberVOCache.get(lottoNumber);
        else {
            LottoNumber lottoNumberVO = new LottoNumber(lottoNumber);
            lottoNumberVOCache.put(lottoNumber, lottoNumberVO);
            return lottoNumberVO;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LottoNumber lottoNumber = (LottoNumber) o;
        return Objects.equals(this.lottoNumber, lottoNumber.lottoNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lottoNumber);
    }
}
