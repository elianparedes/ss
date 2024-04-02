def render():
    video_width = 800
    video_height = 800
    video_builder = VideoBuilder().set_width(video_width).set_height(video_height)

    most_recent_csv = '../output/visitors/offlatice_02_04_2024_01_26_58.csv'

    data = pd.read_csv(most_recent_csv)

    timesteps = data['time'].unique()

    simulation_grid_size = 10
    chart_points = []

    for timestep in timesteps:
        timestep_data = data[data['time'] == timestep]

        video_builder.create_frame()

        # draw_visiting_area(video_builder, timestep_data['visiting_area_x'].iloc[0],
        #                    timestep_data['visiting_area_y'].iloc[0],
        #                    timestep_data['visiting_area_radius'].iloc[0],
        #                    simulation_grid_size)

        draw_particles(timestep_data, simulation_grid_size, video_builder, False)
        draw_timestep(video_builder, timestep)

        draw_polarization_chart(chart_points, len(timesteps), timestep, timestep_data['va'].iloc[0], video_builder)

        video_builder.push_frame()

    video_builder.render()


render()