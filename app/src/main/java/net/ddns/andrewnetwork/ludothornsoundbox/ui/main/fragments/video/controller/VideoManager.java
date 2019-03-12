package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.controller;


import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class VideoManager {

    /**
     * @param videoList list of videos
     * @param id        video id
     * @return video
     */
    public static LudoVideo findVideoById(List<LudoVideo> videoList, String id) {
        for (LudoVideo video : videoList) if (video.getId().equals(id)) return video;
        return null;
    }

    /**
     * @param videoList list of videos
     * @param url       URL
     * @return video found
     */
    public static LudoVideo findVideoByUrl(List<LudoVideo> videoList, String url, boolean next) {
        for (LudoVideo video : videoList)
            if (video.getThumbnail()!= null && video.getThumbnail().getUrl().equals(url))
                if(!next)
                    return video;
                else {
                    next = false;
                    continue;
                }
        return null;
    }

    /**
     * @param videoList list of videos
     * @param oldVideo  old video
     * @param newVideo  new video
     */
    public static void replaceVideo(List<LudoVideo> videoList, LudoVideo oldVideo, LudoVideo newVideo) {
        int index = videoList.indexOf(findVideoById(videoList, oldVideo.getId()));
        videoList.remove(oldVideo);
        videoList.add(index, newVideo);
    }

    /**
     * @param videoList list of videos
     * @param thumbnail new thumbnail
     */
    public static boolean addThumbnailtoVideo(List<LudoVideo> videoList, Thumbnail thumbnail) {
        if(thumbnail!=null) {
            LudoVideo video = findVideoByUrl(videoList, thumbnail.getUrl(), false);
            if (video != null && video.hasThumbnail())
                video = findVideoByUrl(videoList, thumbnail.getUrl(), true);
            if (video != null) {
                video.setThumbnail(thumbnail);
                return true;
            }
        }
        return false;
    }

    /**
     * @param videoList list of videos
     * @return are all thumbnails loaded?
     */
    public static boolean areAllThumbnailsLoaded(List<LudoVideo> videoList) {
        int counter = 0;
        for (LudoVideo video : videoList)
            if (video.getThumbnail()!= null && video.getThumbnail().getImage() != null) counter++;
        if (counter >= videoList.size()) return true;
        return false;
    }

    /**
     * @param videoList lista dei video
     */
    public static ArrayList<LudoVideo> orderByDate(ArrayList<LudoVideo> videoList) {
        LudoVideo video;
        if (videoList.size() > 1)
            for (int i = 0; i < videoList.size() - 1; i++) {
                for (int j = 0; j < videoList.size() - 1; j++) {

                    if (videoList.get(i).getDateTime().compareTo(videoList.get(j).getDateTime()) > 0) {
                        video = videoList.get(i);
                        videoList.set(i, videoList.get(j));
                        videoList.set(j, video);
                    }
                }
            }
            return videoList;
    }

    public static ArrayList<LudoVideo> removeDuplicates (ArrayList<LudoVideo> videoList) {
        if (videoList.size() > 1)
            for (int i = 0; i < videoList.size(); i++)
                for(int j = 0; j < videoList.size(); j++)
                    if((videoList.get(i).compareTo(videoList.get(j)))==0 && i!=j) videoList.remove(j);
        return videoList;
    }

    public static boolean areThereDuplicates(ArrayList<LudoVideo> videoList) {
        if (videoList.size() > 1)
            for (int i = 0; i < videoList.size() - 1; i++)
                for(int j = 0; j < videoList.size() -1; j++)
                    if((videoList.get(i).compareTo(videoList.get(j)))==0 && i!=j) return true;
        return false;
    }

    /**
     * MIGHT CAUSE INFINITE LOOP
     * @param videoList list of videos
     * @return list of videos
     */
    public static ArrayList<LudoVideo> removeDuplicatesifThereAreAny(ArrayList<LudoVideo> videoList) {
        while (true) {
            if (areThereDuplicates(videoList)) {
            removeDuplicates(videoList);
            removeDuplicatesifThereAreAny(videoList);
        } else break;
    }
        return videoList;
    }

    public static List<LudoVideo> addListWithoutDuplicates (List<LudoVideo> videoList, List<LudoVideo> newList) {
        for(LudoVideo video : newList)
            for(LudoVideo video2 : videoList)
                if(video==video2)
                    newList.remove(video);
        videoList.addAll(newList);
        return videoList;

    }

    public static List<LudoVideo> filterByChannel(List<LudoVideo> videoList, Channel channel) {
        List<LudoVideo> filteredVideoList = new ArrayList<>();
        for(LudoVideo video : videoList)
            if(video.getChannel().compareTo(channel)==0)
                filteredVideoList.add(video);
        return filteredVideoList;
    }

    public static ArrayList<LudoVideo> removeVideosWithNoThumbanails(ArrayList<LudoVideo> videoList){
        ArrayList<LudoVideo> videos = new ArrayList<>(videoList);
        try {
            for(LudoVideo video : videoList) if (!video.hasThumbnail()) videos.remove(video);
        } catch (ConcurrentModificationException e){
            e.printStackTrace();
        }

        return videos;
    }

    public static ArrayList<LudoVideo> removeVideosWithNoInformation(List<LudoVideo> videoList){
        ArrayList<LudoVideo> videos = new ArrayList<>(videoList);
        for(LudoVideo video : videoList) if (!video.hasInformation()) videos.remove(video);
        return videos;
    }

    public static ArrayList<LudoVideo> removeAllThumbnails(List<LudoVideo> videoList) {
        ArrayList<LudoVideo> videos = new ArrayList<>(videoList);
        for(LudoVideo video : videoList) if(video.hasThumbnail()) video.setThumbnail(null);
        return videos;
    }
    public static void cleanVideoList(ArrayList<LudoVideo> videoList){
        try {
            videoList = removeVideosWithNoThumbanails(videoList);
            videoList = removeVideosWithNoInformation(videoList);
            videoList = removeDuplicates(videoList);
            videoList = removeAllThumbnails(videoList);
        } catch(ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    /*public static boolean saveToMemory(Context context, LudoVideo video) throws JSONException {
        MemoryManager memoryManager = new MemoryManager(context);
        return memoryManager.saveToMemory(video);
    }
    public static boolean saveToMemory(Context context, List<LudoVideo> videoList) throws JSONException {
        MemoryManager memoryManager = new MemoryManager(context);
        return (memoryManager.saveToMemory(videoList));
    }

    public static List<LudoVideoResponse> getThumbnailsPaths(Context context) throws JSONException {
        MemoryManager memoryManager = new MemoryManager(context);
        return memoryManager.getVideos();
    }

    public static boolean loadThumbnailsFromMemory(Context context, List<LudoVideo> videoList) throws JSONException {
        int counter=0;
        List<LudoVideoResponse> ludoVideoResponseList = getThumbnailsPaths(context);
        for(LudoVideoResponse ludoVideoResponse : ludoVideoResponseList)
            for(LudoVideo video : videoList)
                if(!video.hasThumbnail() && !ludoVideoResponse.getThumbnailPath().isEmpty() && ludoVideoResponse.getId().equals(video.getId())) {
                    video.setThumbnail(new Thumbnail(ludoVideoResponse.getThumbnailPath(), ludoVideoResponse.getUrl()));
                    counter++;
                }
                if(counter>=videoList.size()) return true;
        return false;
    }*/

    public static boolean noVideohasThumbnail(List<LudoVideo> videoList) {
        int counter = 1;
        for(LudoVideo video : videoList) if(!video.hasThumbnail()) counter++;
        if(counter>=videoList.size()) return true;
        return false;
    }

    public static ArrayList<LudoVideo> createCompleteList(List<Channel> channels) {
        ArrayList<LudoVideo> videoList = new ArrayList<>();
        for(Channel channel : channels)
            videoList.addAll(channel.getVideoList());
        removeDuplicates(videoList);
        orderByDate(videoList);
        return videoList;
    }

    public static Integer AllVideosSize(List<Channel> channels) {
        int size=0;
        for(Channel channel : channels)
            size+=channel.getVideoList().size();
        return size;
    }

    public static String buildVideoUrl(String id){
        return "https://www.youtube.com/watch?v="+id;
    }
}
