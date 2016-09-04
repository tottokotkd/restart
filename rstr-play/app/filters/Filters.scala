package filters

import com.google.inject.Inject
import org.pac4j.play.filters.SecurityFilter
import play.api.http.HttpFilters

/**
  * Created by tottokotkd on 18/08/2016.
  */

class Filters @Inject()(securityFilter: SecurityFilter) extends HttpFilters {

  def filters = Seq(securityFilter)

}
