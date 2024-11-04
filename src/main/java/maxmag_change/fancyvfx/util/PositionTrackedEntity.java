package maxmag_change.fancyvfx.util;

import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;

import java.util.List;

public interface PositionTrackedEntity {
	List<TrailPoint> getPastPositions();
}
