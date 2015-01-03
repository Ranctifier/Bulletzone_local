package colin.bulletzone_colin_local.rest;

import org.androidannotations.annotations.rest.Delete;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;

import colin.bulletzone_colin_local.wrapper.BooleanWrapper;
import colin.bulletzone_colin_local.wrapper.GridWrapper;
import colin.bulletzone_colin_local.wrapper.LongWrapper;

/**
 * Annotation used to specify root url. Connects to a bulletzone server.
 */
@Rest(rootUrl = "http://stman1.cs.unh.edu:6191/games",
		converters = {MappingJackson2HttpMessageConverter.class}
)

/**
 * Created by Colin on 11/14/14.
 */
public interface BulletZoneRestClient extends RestClientErrorHandling {
	/**
	 * Sets the base url, related to the root url annotation.
	 *
	 * @param rootUrl
	 */
	void setRootUrl(String rootUrl);

	/**
	 * The join command for a Rest interface, client uses this to enter a
	 * bulletzone game running on a bulletzone server.
	 *
	 * @return
	 * @throws RestClientException
	 */
	@Post("")
	LongWrapper join() throws RestClientException;

	/**
	 * The grid command for a Rest interface, client uses this to obtain the
	 * joined game's state, a grid of data.
	 *
	 * @return
	 */
	@Get("")
	GridWrapper grid();

	/**
	 * The move command for a Rest interface, client uses this to move their
	 * tank forward or backward, the actual movement from one grid cell to
	 * another depends on the tank's directional orientation.
	 *
	 * @param tankId
	 * @param direction
	 * @return
	 */
	@Put("/{tankId}/move/{direction}")
	BooleanWrapper move(long tankId, byte direction) throws RestClientException;

	/**
	 * The turn command for a Rest interface, client uses this to turn their
	 * tank clockwise or counter clockwise which changes the tank's directional
	 * orientation.
	 *
	 * @param tankId
	 * @param direction
	 * @return
	 */
	@Put("/{tankId}/turn/{direction}")
	BooleanWrapper turn(long tankId, byte direction) throws RestClientException;

	/**
	 * The fire command for a Rest interface, client uses this to fire a bullet.
	 * The bullet will move forward based on the directional orientation of the
	 * tank when it was fired.
	 *
	 * @param tankId
	 * @return
	 */
	@Put("/{tankId}/fire")
	BooleanWrapper fire(long tankId) throws RestClientException;

	/**
	 * The leave command for a Rest interface, client uses this to leave a
	 * bulletzone game.
	 *
	 * @param tankId
	 * @return
	 */
	@Delete("/{tankId}/leave")
	BooleanWrapper leave(long tankId);
}
