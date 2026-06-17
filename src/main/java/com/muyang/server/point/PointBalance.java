package com.muyang.server.point;

import com.muyang.server.point.PointTransaction;

import java.util.List;

public record PointBalance(int balance, List<PointTransaction> history) {
}
